package pt.ulisboa.tecnico.classes.classserver;

import io.grpc.*;

import pt.ulisboa.tecnico.classes.classserver.Classe.*;
import pt.ulisboa.tecnico.classes.contract.naming.ClassServerNamingServer;
import pt.ulisboa.tecnico.classes.contract.naming.NamingServerServiceGrpc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ClassServer {

  public static void main(String[] args) throws Exception {
    final int namingPort = 5000;
    final String servico = args[0];
    final String host = args[1];
    final String port = args[2];
    final int serverport = Integer.parseInt(port);
    final List<String> qualificador = new ArrayList<>();
    qualificador.add(args[3]);

    System.out.println(ClassServer.class.getSimpleName());

    Classe classe = new Classe();

    if(args.length >= 5) {
      if (args[3].equals("-debug")) {
        classe.setMode();
      }
    }

    final ManagedChannel channel = ManagedChannelBuilder.forAddress(host, namingPort).usePlaintext().build();

    NamingServerServiceGrpc.NamingServerServiceBlockingStub stub = NamingServerServiceGrpc.newBlockingStub(channel);

    stub.register(ClassServerNamingServer.RegisterRequest.newBuilder().
            setNomeServico(servico).setHost(host).setPort(port).addAllQualificadores(qualificador).build());

    final BindableService classImpl = new ClassServiceImpl(classe);
    final BindableService profImpl = new ProfessorServiceImpl(classe);
    final BindableService adminImpl = new AdminServiceImpl(classe);
    final BindableService studentImpl = new StudentServiceImpl(classe);

    Server server = null;
    try {
      // Create a new server to listen on port.
      server = ServerBuilder.forPort(serverport).addService(classImpl).
              addService(profImpl).
              addService(adminImpl).
              addService(studentImpl).
              build();
      // Start the server.
      server.start();
      // Server threads are running in the background.
      System.out.println("Server started");

      //Create Timer
      Timer timer = new Timer();
      //Create TimerTask
      TimerTask gossip = new Gossip(stub, classe);
      if(args[3].equals("P")) {
        //Schedule Task for running
        timer.schedule(gossip, 5000, 45000);
      }

      System.out.println("Press enter to shutdown");
      System.in.read();

      stub.delete(ClassServerNamingServer.DeleteRequest.newBuilder().
              setNomeServico(servico).setHost(host).setPort(port).build());

      if (args[3].equals("P")) {
        // kill gossip if server is going to shut down
        gossip.cancel();
      }
      //kill timer since it is still running with no tasks
      timer.cancel();
      //removes all cancelled tasks
      timer.purge();

      server.shutdown();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (server != null)
        stub.delete(ClassServerNamingServer.DeleteRequest.newBuilder().
                setNomeServico(servico).setHost(host).setPort(port).build());
        server.shutdown();
    }


  }
}