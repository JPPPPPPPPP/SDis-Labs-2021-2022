package pt.ulisboa.tecnico.classes.admin;

import java.util.Scanner;
import java.util.StringTokenizer;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class Admin {

  private static final String EXIT_CMD = "exit";
  private static final String DUMP_CMD = "dump";
  private static final String LOOK_CMD = "lookup";
  private static final String ACTIVATE_CMD = "activate";
  private static final String DEACTIVATE_CMD = "deactivate";

  public static void main(String[] args) {
    System.out.println(Admin.class.getSimpleName());

    final String host = "localhost";
    final int port = 5000;

    final ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();

    // Create an admin frontend.
    AdminFrontend frontend = new AdminFrontend(channel);

    Scanner scanner = new Scanner(System.in);

    while (true) {
      System.out.printf("%n> ");

      String line = scanner.nextLine();

      String[] tokens = line.split(" ");

      // exit
      if (EXIT_CMD.equals(tokens[0])) {
        scanner.close();
        break;
      }
      else if (LOOK_CMD.equals(tokens[0])) {
        if (tokens.length >= 3)
          frontend.Lookup(tokens[1], tokens[2]);
      }
      else if (DUMP_CMD.equals(tokens[0])) {
        if (tokens.length >= 2) {
          frontend.Dump(tokens[1]);
        }
        else {
          frontend.Dump("P");
        }

      }
      else if (ACTIVATE_CMD.equals(tokens[0])) {
        if (tokens.length >= 2) {
          frontend.activate(tokens[1]);
        }
        else {
          frontend.activate("P");

        }
      }
      else if (DEACTIVATE_CMD.equals(tokens[0])) {
        if (tokens.length >= 2) {
          frontend.deactivate(tokens[1]);
        }
        else {
          frontend.deactivate("P");
        }
      }
    }
    channel.shutdownNow();
  }
}
