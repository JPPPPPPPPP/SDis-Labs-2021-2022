package pt.ulisboa.tecnico.classes.admin;

import java.util.Scanner;
import java.util.StringTokenizer;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class Admin {

  private static final String EXIT_CMD = "exit";
  private static final String DUMP_CMD = "dump";

  public static void main(String[] args) {
    System.out.println(Admin.class.getSimpleName());

    final String host = "localhost";
    final int port = 8080;

    final ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();

    // Create an admin frontend.
    AdminFrontend frontend = new AdminFrontend(channel);

    Scanner scanner = new Scanner(System.in);

    while (true) {
      System.out.printf("%n> ");

      String line = scanner.nextLine();

      // exit
      if (EXIT_CMD.equals(line)) {
        scanner.close();
        break;
      }
      else if (DUMP_CMD.equals(line)) {
        frontend.Dump();
      }
    }
    channel.shutdownNow();
  }
}
