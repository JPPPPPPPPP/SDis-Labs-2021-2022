package pt.ulisboa.tecnico.classes.professor;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import pt.ulisboa.tecnico.classes.NamingFrontend;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions;
import pt.ulisboa.tecnico.classes.contract.naming.NamingServerServiceGrpc;
import pt.ulisboa.tecnico.classes.contract.professor.ProfessorClassServer;
import pt.ulisboa.tecnico.classes.contract.professor.ProfessorServiceGrpc;
import pt.ulisboa.tecnico.classes.Stringify;

import java.util.ArrayList;
import java.util.List;

import static pt.ulisboa.tecnico.classes.contract.professor.ProfessorClassServer.*;


public class ProfessorFrontend {
    NamingServerServiceGrpc.NamingServerServiceBlockingStub stub;

    public ProfessorFrontend(ManagedChannel channel) {
        stub = NamingServerServiceGrpc.newBlockingStub(channel);
    }

    public void openEnrollments(String cap) {
        ProfessorServiceGrpc.ProfessorServiceBlockingStub ligacao;
        List<String> qualifiers = new ArrayList<>();
        qualifiers.add("P");
        List<ClassesDefinitions.ServerEntryState> response = NamingFrontend.Lookup(stub, "Turmas", qualifiers);
        for (ClassesDefinitions.ServerEntryState s : response) {
            ManagedChannel channel = ManagedChannelBuilder.forAddress(s.getHost(), Integer.parseInt(s.getPort())).usePlaintext().build();
            ligacao = ProfessorServiceGrpc.newBlockingStub(channel);
            OpenEnrollmentsResponse ret = ligacao.openEnrollments(ProfessorClassServer.OpenEnrollmentsRequest.newBuilder().
                    setCapacity(Integer.parseInt(cap)).build());
            System.out.println(Stringify.format(ret.getCode()));
        }
    }

    public void closeEnrollments() {
        ProfessorServiceGrpc.ProfessorServiceBlockingStub ligacao;
        List<String> qualifiers = new ArrayList<>();
        qualifiers.add("P");
        List<ClassesDefinitions.ServerEntryState> response = NamingFrontend.Lookup(stub, "Turmas", qualifiers);
        for (ClassesDefinitions.ServerEntryState s : response) {
            ManagedChannel channel = ManagedChannelBuilder.forAddress(s.getHost(), Integer.parseInt(s.getPort())).usePlaintext().build();
            ligacao = ProfessorServiceGrpc.newBlockingStub(channel);
            CloseEnrollmentsResponse ret = ligacao.closeEnrollments(ProfessorClassServer.CloseEnrollmentsRequest.newBuilder().build());
            System.out.println(Stringify.format(ret.getCode()));
        }
    }

    public void listClass() {
        ProfessorServiceGrpc.ProfessorServiceBlockingStub ligacao;
        List<String> qualifiers = new ArrayList<>();
        qualifiers.add("S");
        List<ClassesDefinitions.ServerEntryState> responseS = NamingFrontend.Lookup(stub, "Turmas", qualifiers);
        for (ClassesDefinitions.ServerEntryState s : responseS) {
            ManagedChannel channel = ManagedChannelBuilder.forAddress(s.getHost(), Integer.parseInt(s.getPort())).usePlaintext().build();
            ligacao = ProfessorServiceGrpc.newBlockingStub(channel);
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
            ligacao = ProfessorServiceGrpc.newBlockingStub(channel);
            ListClassRequest request = ListClassRequest.newBuilder().build();
            ListClassResponse ret = ligacao.listClass(request);
            if (Stringify.format(ret.getCode()).equals("The action completed successfully.")) {
                System.out.println(Stringify.format(ret.getClassState()));
                return;
            }
            System.out.println(Stringify.format(ret.getCode())+"\n");
        }

    }

    public void cancelEnrollment(String studentId) {
        ProfessorServiceGrpc.ProfessorServiceBlockingStub ligacao;
        List<String> qualifiers = new ArrayList<>();
        qualifiers.add("P");
        List<ClassesDefinitions.ServerEntryState> response = NamingFrontend.Lookup(stub, "Turmas", qualifiers);
        for (ClassesDefinitions.ServerEntryState s : response) {
            ManagedChannel channel = ManagedChannelBuilder.forAddress(s.getHost(), Integer.parseInt(s.getPort())).usePlaintext().build();
            ligacao = ProfessorServiceGrpc.newBlockingStub(channel);
            CancelEnrollmentResponse ret = ligacao.cancelEnrollment(ProfessorClassServer.CancelEnrollmentRequest.newBuilder().setStudentId(studentId).build());
            System.out.println(Stringify.format(ret.getCode()));
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
