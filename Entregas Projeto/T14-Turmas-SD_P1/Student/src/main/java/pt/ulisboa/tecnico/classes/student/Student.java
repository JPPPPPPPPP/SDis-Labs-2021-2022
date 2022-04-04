package pt.ulisboa.tecnico.classes.student;
import java.util.Scanner;
import java.util.StringTokenizer;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import pt.ulisboa.tecnico.classes.contract.student.*;
//import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions.Student;
//import pt.ulisboa.tecnico.classes.classserver;


public class Student{

  private static final String EXIT_CMD = "exit";
  private static final String ENROLL_CMD = "enroll";
  private static final String LIST_CMD = "list";

  public static void main(String[] args) {
    System.out.println(Student.class.getSimpleName());
    System.out.printf("Received %d Argument(s)%n", args.length);
    for (int i = 0; i < args.length; i++) {
      System.out.printf("args[%d] = %s%n", i, args[i]);
    }


    //checks arguements
    if(args.length < 2) {
      System.out.println("Arguements invalid");
      return;
    }

    String strId = args[0];
    String strName = "";
    for (int xd = 1; xd < args.length; xd++) {
      strName = strName + args[xd];
      if (xd+1 != args.length) {
        strName = strName + " ";
      }
    }

    final String host = "localhost";
    final int port = 8080;

    final ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();

    //create blocking stub
    final StudentServiceGrpc.StudentServiceBlockingStub stub = StudentServiceGrpc.newBlockingStub(channel);
    StudentFrontend frontend = new StudentFrontend(channel);
    Scanner scanner = new Scanner(System.in);

    //loop to accept commands
    while(true) {
      System.out.printf("\n> ");

      String line = scanner.nextLine();

      if(line.equals(EXIT_CMD)) {
        scanner.close();
        break;
      }

      if(line.equals(LIST_CMD)) {
        frontend.listClass();
      }

      else if(line.equals(ENROLL_CMD)) {
        frontend.Enroll(strId,strName);
      }
    }
    channel.shutdownNow();
  }
}