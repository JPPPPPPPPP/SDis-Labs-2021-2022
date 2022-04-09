package pt.ulisboa.tecnico.classes.classserver;

import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer;
import pt.ulisboa.tecnico.classes.contract.professor.ProfessorClassServer;
import pt.ulisboa.tecnico.classes.contract.professor.ProfessorClassServer.*;
import pt.ulisboa.tecnico.classes.contract.professor.ProfessorServiceGrpc;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions.*;

import io.grpc.stub.StreamObserver;
import pt.ulisboa.tecnico.classes.contract.student.StudentClassServer;

public class ProfessorServiceImpl extends ProfessorServiceGrpc.ProfessorServiceImplBase {
    private Classe classe;

    public ProfessorServiceImpl(Classe c) {
        this.classe = c;
    }

    @Override
    public void openEnrollments(OpenEnrollmentsRequest request, StreamObserver<OpenEnrollmentsResponse> responseObserver) {
        if (classe.getMode()) {
            System.err.printf("Received an openEnrollments request from Professor with this capacity: " + request.getCapacity() + "\n\n");
        }
        if(!classe.isActivated()) {
            responseObserver.onNext(OpenEnrollmentsResponse.newBuilder().setCode(ResponseCode.INACTIVE_SERVER).build());
            responseObserver.onCompleted();
            return;
        }
        if(classe.getClassState().getOpenEnrollments()) {
            responseObserver.onNext(OpenEnrollmentsResponse.newBuilder().setCode(ResponseCode.ENROLLMENTS_ALREADY_OPENED).build());
            responseObserver.onCompleted();
            return;
        }
        if(classe.getClassState().getEnrolledStudents().size() >= request.getCapacity()) {
            responseObserver.onNext(OpenEnrollmentsResponse.newBuilder().setCode(ResponseCode.FULL_CLASS).build());
            responseObserver.onCompleted();
            return;
        }
        classe.openEnrollments(request.getCapacity());

        responseObserver.onNext(OpenEnrollmentsResponse.newBuilder().setCode(ResponseCode.OK).build());
        responseObserver.onCompleted();
    }

    @Override
    public void closeEnrollments(CloseEnrollmentsRequest request, StreamObserver<CloseEnrollmentsResponse> responseObserver) {
        if(classe.getMode()) {
            System.err.printf("Received a closeEnrollments request from Professor\n\n");
        }
        if(!classe.isActivated()) {
            responseObserver.onNext(CloseEnrollmentsResponse.newBuilder().setCode(ResponseCode.INACTIVE_SERVER).build());
            responseObserver.onCompleted();
            return;
        }
        if(classe.getClassState().getOpenEnrollments() == false) {
            responseObserver.onNext(CloseEnrollmentsResponse.newBuilder().setCode(ResponseCode.ENROLLMENTS_ALREADY_CLOSED).build());
            responseObserver.onCompleted();
            return;
        }
        classe.closeEnrollments();

        responseObserver.onNext(CloseEnrollmentsResponse.newBuilder().setCode(ResponseCode.OK).build());
        responseObserver.onCompleted();
    }

    @Override
    public void listClass(ListClassRequest request, StreamObserver<ListClassResponse> responseObserver) {
        if(!classe.isActivated()) {
            responseObserver.onNext(ListClassResponse.newBuilder().setCode(ResponseCode.INACTIVE_SERVER).build());
            responseObserver.onCompleted();
            return;
        }

        ClassState classState = classe.getClassState();

        responseObserver.onNext(ListClassResponse.newBuilder().setCode(ResponseCode.OK).setClassState(classState.asMessage()).build());
        if (classe.getMode()) {
            System.err.printf("Received a list request from Professor\n\n");
        }
        responseObserver.onCompleted();
    }

    @Override
    public void cancelEnrollment(CancelEnrollmentRequest request, StreamObserver<CancelEnrollmentResponse> responseObserver) {
        if (classe.getMode()) {
            System.err.printf("Received an cancelEnrollments request from Professor with this studentId: " + request.getStudentId() + "\n\n");
        }
        if(!classe.isActivated()) {
            responseObserver.onNext(CancelEnrollmentResponse.newBuilder().setCode(ResponseCode.INACTIVE_SERVER).build());
            responseObserver.onCompleted();
            return;
        }

        if(classe.isEnrolledStudent(request.getStudentId()) == false) {
            responseObserver.onNext(CancelEnrollmentResponse.newBuilder().setCode(ResponseCode.NON_EXISTING_STUDENT).build());
            responseObserver.onCompleted();
            return;
        }
        classe.cancelEnrollment(request.getStudentId());

        responseObserver.onNext(CancelEnrollmentResponse.newBuilder().setCode(ResponseCode.OK).build());
        responseObserver.onCompleted();
    }

}
