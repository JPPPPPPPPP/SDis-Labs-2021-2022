package pt.ulisboa.tecnico.classes.professor;

import io.grpc.ManagedChannel;
import pt.ulisboa.tecnico.classes.contract.professor.ProfessorClassServer;
import pt.ulisboa.tecnico.classes.contract.professor.ProfessorServiceGrpc;
import pt.ulisboa.tecnico.classes.professor.ProfessorFrontend.*;
import pt.ulisboa.tecnico.classes.contract.professor.*;
import pt.ulisboa.tecnico.classes.Stringify;

import static pt.ulisboa.tecnico.classes.contract.professor.ProfessorClassServer.*;


public class ProfessorFrontend {
    ProfessorServiceGrpc.ProfessorServiceBlockingStub stub;

    public ProfessorFrontend(ManagedChannel channel) {
        stub = ProfessorServiceGrpc.newBlockingStub(channel);
    }

    public void openEnrollments(String cap) {
        OpenEnrollmentsResponse response = stub.openEnrollments(ProfessorClassServer.OpenEnrollmentsRequest.newBuilder().setCapacity(Integer.parseInt(cap)).build());
        System.out.println(Stringify.format(response.getCode()));
    }

    public void closeEnrollments() {
        CloseEnrollmentsResponse response = stub.closeEnrollments(ProfessorClassServer.CloseEnrollmentsRequest.newBuilder().build());
        System.out.println(Stringify.format(response.getCode()));
    }

    public void listClass() {
        ListClassRequest request = ListClassRequest.newBuilder().build();
        ListClassResponse response = stub.listClass(request);
        System.out.println(Stringify.format(response.getClassState()));
    }

    public void cancelEnrollment(String studentId) {
        CancelEnrollmentResponse response = stub.cancelEnrollment(ProfessorClassServer.CancelEnrollmentRequest.newBuilder().setStudentId(studentId).build());
        System.out.println(Stringify.format(response.getCode()));
    }
}
