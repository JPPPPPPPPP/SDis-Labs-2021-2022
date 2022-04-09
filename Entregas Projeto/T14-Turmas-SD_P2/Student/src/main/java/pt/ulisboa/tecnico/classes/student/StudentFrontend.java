package pt.ulisboa.tecnico.classes.student;

import io.grpc.ManagedChannel;
import pt.ulisboa.tecnico.classes.NamingFrontend;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions;
import pt.ulisboa.tecnico.classes.contract.naming.NamingServerServiceGrpc;
import pt.ulisboa.tecnico.classes.contract.student.StudentClassServer;
import pt.ulisboa.tecnico.classes.contract.student.StudentServiceGrpc;
import static pt.ulisboa.tecnico.classes.contract.student.StudentClassServer.*;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions.Student;
import pt.ulisboa.tecnico.classes.student.Student.*;


import io.grpc.ManagedChannelBuilder;
import pt.ulisboa.tecnico.classes.contract.student.*;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions.Student;
import pt.ulisboa.tecnico.classes.Stringify;

import java.util.ArrayList;
import java.util.List;

public class StudentFrontend {

    NamingServerServiceGrpc.NamingServerServiceBlockingStub stub;

    public StudentFrontend(ManagedChannel channel){
        stub = NamingServerServiceGrpc.newBlockingStub(channel);
    }

    public void Enroll(String id, String nome) {
        if(id.length() != 9 ||
                !id.startsWith("aluno") ||
                !isInteger(id.substring(id.length()-4)) ||
                nome.length() <3 ||
                nome.length() > 30) {
            return;
        }
        StudentServiceGrpc.StudentServiceBlockingStub ligacao;
        List<String> qualifiers = new ArrayList<>();
        qualifiers.add("P");
        List<ClassesDefinitions.ServerEntryState> response = NamingFrontend.Lookup(stub, "Turmas", qualifiers);
        for (ClassesDefinitions.ServerEntryState s : response) {
            ManagedChannel channel = ManagedChannelBuilder.forAddress(s.getHost(), Integer.parseInt(s.getPort())).usePlaintext().build();
            ligacao = StudentServiceGrpc.newBlockingStub(channel);
            StudentClassServer.EnrollResponse ret = ligacao.enroll(StudentClassServer.EnrollRequest.newBuilder().setStudent(Student.newBuilder().setStudentId(id).setStudentName(nome).build()).build());
            System.out.println(Stringify.format(ret.getCode()));
        }
    }

    public void listClass() {
        StudentServiceGrpc.StudentServiceBlockingStub ligacao;
        List<String> qualifiers = new ArrayList<>();
        qualifiers.add("S");
        List<ClassesDefinitions.ServerEntryState> responseS = NamingFrontend.Lookup(stub, "Turmas", qualifiers);
        for (ClassesDefinitions.ServerEntryState s : responseS) {
            ManagedChannel channel = ManagedChannelBuilder.forAddress(s.getHost(), Integer.parseInt(s.getPort())).usePlaintext().build();
            ligacao = StudentServiceGrpc.newBlockingStub(channel);
            ListClassRequest request = ListClassRequest.newBuilder().build();
            ListClassResponse ret = ligacao.listClass(request);
            if (Stringify.format(ret.getCode()).equals("The action completed successfully.")) {
                System.out.println(Stringify.format(ret.getClassState()));
                return;
            }
            System.out.println(Stringify.format(ret.getCode())+"\n");
        }
        qualifiers.remove("S");
        qualifiers.add("P");
        List<ClassesDefinitions.ServerEntryState> responseP = NamingFrontend.Lookup(stub, "Turmas", qualifiers);
        for (ClassesDefinitions.ServerEntryState s : responseP) {
            ManagedChannel channel = ManagedChannelBuilder.forAddress(s.getHost(), Integer.parseInt(s.getPort())).usePlaintext().build();
            ligacao = StudentServiceGrpc.newBlockingStub(channel);
            ListClassRequest request = ListClassRequest.newBuilder().build();
            ListClassResponse ret = ligacao.listClass(request);
            if (Stringify.format(ret.getCode()).equals("The action completed successfully.")) {
                System.out.println(Stringify.format(ret.getClassState())+"\n");
                return;
            }
            System.out.println(Stringify.format(ret.getCode()));
        }
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

    public void Lookup(String servico, String qualificadores) {
        if (qualificadores.equals("placeholder")) {
            List<String> qualifiers = new ArrayList<>();
            List<ClassesDefinitions.ServerEntryState> response = NamingFrontend.Lookup(stub, servico, qualifiers);
            for (ClassesDefinitions.ServerEntryState s : response) {
                System.out.println(Stringify.format(s));
            }
            return;
        }
        List<String> qualifiers = new ArrayList<>();
        qualifiers.add(qualificadores);
        List<ClassesDefinitions.ServerEntryState> response = NamingFrontend.Lookup(stub, servico, qualifiers);
        for (ClassesDefinitions.ServerEntryState s : response) {
            System.out.println(Stringify.format(s));
        }
    }
}