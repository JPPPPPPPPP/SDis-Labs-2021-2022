package pt.tecnico.supplier.client;

import static javax.xml.bind.DatatypeConverter.printHexBinary;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import pt.tecnico.supplier.grpc.ProductsRequest;
import pt.tecnico.supplier.grpc.ProductsResponse;
import pt.tecnico.supplier.grpc.SupplierGrpc;
import pt.tecnico.supplier.grpc.SignedResponse;
import pt.tecnico.supplier.grpc.Signature;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;

import javax.crypto.*;
import java.security.*;
import java.util.Arrays;

public class SupplierClient {

	static String DIGEST_ALGO = "SHA-256";
	static String MAC_ALGO = "HmacSHA256";
	String SYM_ALGO = "AES";
	static String SYM_CIPHER = "AES/CBC/PKCS5Padding";
	static int SYM_IV_LEN = 16;

	/**
	 * Set flag to true to print debug messages. The flag can be set using the
	 * -Ddebug command line option.
	 */
	private static final boolean DEBUG_FLAG = (System.getProperty("debug") != null);

	/** Helper method to print debug messages. */
	private static void debug(String debugMessage) {
		if (DEBUG_FLAG)
			System.err.println(debugMessage);
	}

	public static SecretKeySpec readKey(String resourcePathName) throws Exception {
		System.out.println("Reading key from resource " + resourcePathName + " ...");

		InputStream fis = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePathName);
		byte[] encoded = new byte[fis.available()];
		fis.read(encoded);
		fis.close();

		System.out.println("Key:");
		System.out.println(printHexBinary(encoded));
		SecretKeySpec keySpec = new SecretKeySpec(encoded, "AES");

		return keySpec;
	}

	public static void main(String[] args) throws Exception {
		System.out.println(SupplierClient.class.getSimpleName() + " starting ...");
		long previousCounter = -1;
		// Receive and print arguments.
		System.out.printf("Received %d arguments%n", args.length);
		for (int i = 0; i < args.length; i++) {
			System.out.printf("arg[%d] = %s%n", i, args[i]);
		}

		// Check arguments.
		if (args.length < 2) {
			System.err.println("Argument(s) missing!");
			System.err.printf("Usage: java %s host port%n", SupplierClient.class.getName());
			return;
		}

		final String host = args[0];
		final int port = Integer.parseInt(args[1]);
		final String target = host + ":" + port;

		// Channel is the abstraction to connect to a service end-point.
		final ManagedChannel channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();

		// Create a blocking stub for making synchronous remote calls.
		SupplierGrpc.SupplierBlockingStub stub = SupplierGrpc.newBlockingStub(channel);

		// Prepare request.
		ProductsRequest request = ProductsRequest.newBuilder().build();
		System.out.println("Request to send:");
		System.out.println(request.toString());
		debug("in binary hexadecimals:");
		byte[] requestBinary = request.toByteArray();
		debug(printHexBinary(requestBinary));
		debug(String.format("%d bytes%n", requestBinary.length));

		// Make the call using the stub.
		System.out.println("Remote call...");
		SignedResponse response = stub.listProducts(request);

		SecretKeySpec keyspec = null;

		byte[] prodBytes = response.getResponse().toByteArray();
		byte[] sigValueBytes = response.getSignature().getValue().toByteArray();
		byte[] newPlainBytes = new byte[70];
		byte[] cypherDigest = new byte[69];
		MessageDigest md = MessageDigest.getInstance(DIGEST_ALGO);
		try {
			keyspec = readKey("."); //./secretkey if fucked
			Cipher cipher = Cipher.getInstance(SYM_CIPHER);
			byte[] iv = new byte[SYM_IV_LEN];
			IvParameterSpec ips = new IvParameterSpec(iv);
			cipher.init(Cipher.DECRYPT_MODE, keyspec, ips);
			newPlainBytes = cipher.doFinal(sigValueBytes);
			md.update(prodBytes);
			md.digest();
		} catch (Exception e) {
		  e.printStackTrace();
		}

		if(Arrays.equals(newPlainBytes, md.digest())) {
			System.out.println("Signature is valid! Message accepted! :)");
			} else {
			System.out.println("Signature is invalid! Message rejected! :(");
		}

		if(previousCounter >= response.getSignature().getCounter()) {
			return;
		} else {
			previousCounter = response.getSignature().getCounter();
		}

		// Print response.
		System.out.println("Received response:");
		System.out.println(response.toString());
		debug("in binary hexadecimals:");
		byte[] responseBinary = response.toByteArray();
		debug(printHexBinary(responseBinary));
		debug(String.format("%d bytes%n", responseBinary.length));

		// A Channel should be shutdown before stopping the process.
		channel.shutdownNow();
	}


}
