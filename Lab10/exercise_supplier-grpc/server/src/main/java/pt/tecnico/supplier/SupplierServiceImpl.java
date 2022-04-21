package pt.tecnico.supplier;

import static javax.xml.bind.DatatypeConverter.printHexBinary;

import com.google.protobuf.ByteString;
import com.google.type.Money;

import io.grpc.stub.StreamObserver;
import pt.tecnico.supplier.domain.Supplier;
import pt.tecnico.supplier.grpc.Product;
import pt.tecnico.supplier.grpc.ProductsRequest;
import pt.tecnico.supplier.grpc.ProductsResponse;
import pt.tecnico.supplier.grpc.SupplierGrpc;
import pt.tecnico.supplier.grpc.SignedResponse;
import pt.tecnico.supplier.grpc.Signature;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static javax.xml.bind.DatatypeConverter.printHexBinary;

import java.io.InputStream;
import javax.crypto.*;
import java.security.*;

public class SupplierServiceImpl extends SupplierGrpc.SupplierImplBase {

	int counter = 0;
	static String DIGEST_ALGO = "SHA-256";
	static String MAC_ALGO = "HmacSHA256";
	String SYM_ALGO = "AES";
	String SYM_CIPHER = "AES/CBC/PKCS5Padding";

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

	/** Domain object. */
	final private Supplier supplier = Supplier.getInstance();

	/** Constructor */
	public SupplierServiceImpl() {
		debug("Loading demo data...");
		supplier.demoData();
	}

	/** Helper method to convert domain product to message product. */
	private Product buildProductFromProduct(pt.tecnico.supplier.domain.Product p) {
		Product.Builder productBuilder = Product.newBuilder();
		productBuilder.setIdentifier(p.getId());
		productBuilder.setDescription(p.getDescription());
		productBuilder.setQuantity(p.getQuantity());

		Money.Builder moneyBuilder = Money.newBuilder();
		moneyBuilder.setCurrencyCode("EUR").setUnits(p.getPrice());
		productBuilder.setPrice(moneyBuilder.build());

		return productBuilder.build();
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

	@Override
	public void listProducts(ProductsRequest request, StreamObserver<SignedResponse> responseObserver) {
		debug("listProducts called");

		debug("Received request:");
		debug(request.toString());
		debug("in binary hexadecimals:");
		byte[] requestBinary = request.toByteArray();
		debug(String.format("%d bytes%n", requestBinary.length));

		// build ProductsResponse
		ProductsResponse.Builder responseBuilder = ProductsResponse.newBuilder();
		responseBuilder.setSupplierIdentifier(supplier.getId());
		for (String pid : supplier.getProductsIDs()) {
			pt.tecnico.supplier.domain.Product p = supplier.getProduct(pid);
			Product product = buildProductFromProduct(p);
			responseBuilder.addProduct(product);
		}
		ProductsResponse prodResponse = responseBuilder.build();

		//Build signature
		Signature.Builder sigBuilder = Signature.newBuilder();
		SecretKeySpec keyspec = null;
		byte[] cipherBytes = new byte[0];
		try {
			keyspec = readKey("."); //./secretkey if fucked
			byte[] prodBytes = prodResponse.toByteArray();
			MessageDigest md = MessageDigest.getInstance(DIGEST_ALGO);
			md.update(prodBytes);
			md.digest();
			Cipher cipher = Cipher.getInstance(SYM_CIPHER);
			cipher.init(Cipher.ENCRYPT_MODE, keyspec);
			cipherBytes = cipher.doFinal(md.digest());
		} catch (Exception e) {
			e.printStackTrace();
		}
		sigBuilder.setValue(ByteString.copyFrom(cipherBytes));
		sigBuilder.setSignerId(supplier.getId());
		sigBuilder.setCounter(counter);
		Signature sig = sigBuilder.build();

		//Build SignedResponse
		SignedResponse.Builder signedResponseBuilder = SignedResponse.newBuilder();
		signedResponseBuilder.setResponse(prodResponse);
		signedResponseBuilder.setSignature(sig);
		SignedResponse sigResponse = signedResponseBuilder.build();

		//still refers to prodResponse
		debug("Response to send:");
		debug(sigResponse.toString());
		debug("in binary hexadecimals:");
		byte[] responseBinary = sigResponse.toByteArray();
		debug(printHexBinary(responseBinary));
		debug(String.format("%d bytes%n", responseBinary.length));

		//aumentar counter de assinaturas
		counter++;
		// send single response back
		responseObserver.onNext(sigResponse);
		// complete call
		responseObserver.onCompleted();
	}

}
