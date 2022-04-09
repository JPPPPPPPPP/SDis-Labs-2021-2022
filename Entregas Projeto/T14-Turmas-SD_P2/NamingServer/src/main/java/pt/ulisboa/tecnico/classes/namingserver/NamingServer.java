package pt.ulisboa.tecnico.classes.namingserver;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NamingServer {

  private static int port = 5000;

  public static void main(String[] args) throws IOException, InterruptedException {
    System.out.println(NamingServer.class.getSimpleName());
    System.out.printf("Received %d Argument(s)%n", args.length);
    for (int i = 0; i < args.length; i++) {
      System.out.printf("args[%d] = %s%n", i, args[i]);
    }
    //TODO: verify args if necessary

    Naming naming = new Naming();

    final BindableService namingImpl = new NamingServerServiceImpl(naming);

    // Create a new server to listen on port.
    Server server = ServerBuilder.forPort(port).
            addService(namingImpl).
            build();
    // Start the server.
    server.start();
    // Server threads are running in the background.
    System.out.println("Server started");

    // Do not exit the main thread. Wait until server is terminated.
    server.awaitTermination();

  }
}
