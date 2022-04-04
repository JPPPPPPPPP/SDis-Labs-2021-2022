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

  public static void main(String[] args) {

    System.out.println(Professor.class.getSimpleName());

    final String host = "localhost";
    final int port = 8080;

    final ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();

    //creates ProfessorFrontend
    ProfessorFrontend frontend= new ProfessorFrontend(channel);

    //creates scanner
    Scanner scanner = new Scanner(System.in);

    //loop to accept commands
    while(true) {
      System.out.printf("\n> ");
      String line = scanner.nextLine();

      //tokenise arguements
      StringTokenizer st = new StringTokenizer(line);
      String command = st.nextToken();
      //checks if there is an arguement passed as well
      String arg = "placeholder";
      if (st.hasMoreTokens()) {
        arg = st.nextToken();
      }

      if(command.equals(EXIT_CMD)) {
        scanner.close();
        break;
      } else if(command.equals(OPEN_ENROLLMENTS_CMD)) {
        if (arg != "placeholder") {
          frontend.openEnrollments(arg);
        } else {
          //No capacity arguement was passed but no error exists for this
        }
      } else if(command.equals(CLOSE_ENROLLMENTS_CMD)){
        frontend.closeEnrollments();
      } else if(command.equals(LIST_CMD)){
        frontend.listClass();
      } else if(command.equals(CANCEL_ENROLLMENT_CMD)){
        if(arg != "placeholder") {
          frontend.cancelEnrollment(arg);
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
