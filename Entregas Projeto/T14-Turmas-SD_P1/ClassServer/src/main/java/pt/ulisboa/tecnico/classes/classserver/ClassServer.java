package pt.ulisboa.tecnico.classes.classserver;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import pt.ulisboa.tecnico.classes.classserver.Classe.*;

public class ClassServer {

  private static int port = 8080;

  public static void main(String[] args) throws Exception {
    System.out.println(ClassServer.class.getSimpleName());

    Classe classe = new Classe();

    if(args.length >= 1) {
      if (args[0].equals("-debug")) {
        classe.setMode();
      }
    }

    final BindableService classImpl = new ClassServiceImpl(classe);
    final BindableService profImpl = new ProfessorServiceImpl(classe);
    final BindableService adminImpl = new AdminServiceImpl(classe);
    final BindableService studentImpl = new StudentServiceImpl(classe);

    // Create a new server to listen on port.
    Server server = ServerBuilder.forPort(port).addService(classImpl).
            addService(profImpl).
            addService(adminImpl).
            addService(studentImpl).
            build();
    // Start the server.
    server.start();
    // Server threads are running in the background.
    System.out.println("Server started");

    // Do not exit the main thread. Wait until server is terminated.
    server.awaitTermination();


  }
}