package pt.ulisboa.tecnico.classes.professor;

import java.util.Scanner;
import java.util.StringTokenizer;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class Professor {

  //commands
  private static final String EXIT_CMD = "exit";
  private static final String OPEN_ENROLLMENTS_CMD = "openEnrollments";
  private static final String CLOSE_ENROLLMENTS_CMD = "closeEnrollments";
  private static final String LIST_CMD = "list";
  private static final String CANCEL_ENROLLMENT_CMD = "cancelEnrollment";
  private static final String LOOK_CMD = "lookup";

  public static void main(String[] args) {

    System.out.println(Professor.class.getSimpleName());

    final String host = "localhost";
    final int port = 5000;

    final ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();

    //creates ProfessorFrontend
    ProfessorFrontend frontend= new ProfessorFrontend(channel);

    //creates scanner
    Scanner scanner = new Scanner(System.in);

    //loop to accept commands
    while(true) {
      System.out.printf("\n> ");
      String line = scanner.nextLine();

      String[] tokens = line.split(" ");

      if(tokens[0].equals(EXIT_CMD)) {
        scanner.close();
        break;
      } else if(tokens[0].equals(LOOK_CMD)) {
          if (tokens.length >= 3) {
            frontend.Lookup(tokens[1], tokens[2]);
          }
          else {
            frontend.Lookup(tokens[1], "placeholder");
          }
      }else if(tokens[0].equals(OPEN_ENROLLMENTS_CMD)) {
        if (tokens.length >= 2) {
          frontend.openEnrollments(tokens[1]);
        } else {
          //No capacity arguement was passed but no error exists for this
        }
      } else if(tokens[0].equals(CLOSE_ENROLLMENTS_CMD)){
        frontend.closeEnrollments();
      } else if(tokens[0].equals(LIST_CMD)){
        frontend.listClass();
      } else if(tokens[0].equals(CANCEL_ENROLLMENT_CMD)){
        if(tokens.length >= 2) {
          frontend.cancelEnrollment(tokens[1]);
        } else {
          //No student id arguement was passed but no error exists for this
        }
      } else {
        //Command is invalid in this case but no error message exists for this
      }
    }
    channel.shutdownNow();
  }
}
