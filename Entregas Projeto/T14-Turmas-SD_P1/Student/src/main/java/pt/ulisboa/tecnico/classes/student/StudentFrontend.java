package pt.ulisboa.tecnico.classes.student;

import io.grpc.ManagedChannel;
import pt.ulisboa.tecnico.classes.contract.student.StudentClassServer;
import pt.ulisboa.tecnico.classes.contract.student.StudentServiceGrpc;
import static pt.ulisboa.tecnico.classes.contract.student.StudentClassServer.*;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions.Student;
import pt.ulisboa.tecnico.classes.student.Student.*;


import io.grpc.ManagedChannelBuilder;
import pt.ulisboa.tecnico.classes.contract.student.*;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions.Student;
import pt.ulisboa.tecnico.classes.Stringify;

public class StudentFrontend {

    StudentServiceGrpc.StudentServiceBlockingStub stub;

    public StudentFrontend(ManagedChannel channel){
        stub = StudentServiceGrpc.newBlockingStub(channel);
    }

    public void Enroll(String id, String nome) {
        if(id.length() != 9 ||
                !id.startsWith("aluno") ||
                !isInteger(id.substring(id.length()-4)) ||
                nome.length() <3 ||
                nome.length() > 30) {
            return;
        }
        StudentClassServer.EnrollResponse response = stub.enroll(StudentClassServer.EnrollRequest.newBuilder().setStudent(Student.newBuilder().setStudentId(id).setStudentName(nome).build()).build());
        System.out.println(Stringify.format(response.getCode()));
    }

    public void listClass() {
        ListClassRequest request = ListClassRequest.newBuilder().build();
        ListClassResponse response = stub.listClass(request);
        System.out.println(Stringify.format(response.getClassState()));
    }

    public boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        }
        catch(NumberFormatException e) {
            return false;
        }
    }
}