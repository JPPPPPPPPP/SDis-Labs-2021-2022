package pt.ulisboa.tecnico.classes.classserver;
import pt.ulisboa.tecnico.classes.contract.student.StudentClassServer.*;
import pt.ulisboa.tecnico.classes.contract.student.StudentServiceGrpc;

import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions.*;

import io.grpc.stub.StreamObserver;

public class StudentServiceImpl extends StudentServiceGrpc.StudentServiceImplBase {
    private Classe classe;

    public StudentServiceImpl(Classe c) {
        this.classe = c;
    }
    @Override
    public void listClass(ListClassRequest request, StreamObserver<ListClassResponse> responseObserver) {
        ClassState classState = classe.getClassState();

        responseObserver.onNext(ListClassResponse.newBuilder().setCode(ResponseCode.OK).setClassState(classState.asMessage()).build());
        if (classe.getMode()) {
            System.err.printf("Received a list request from Student\n\n");
        }
        responseObserver.onCompleted();
    }
    
    @Override
    public void enroll(EnrollRequest request, StreamObserver<EnrollResponse> responseObserver) {
        if (classe.getMode()) {
            System.err.printf("Received an enroll request from Student with this args: " + request.getStudent() + "\n\n");
        }
        if(!classe.getClassState().getOpenEnrollments()) {
            responseObserver.onNext(EnrollResponse.newBuilder().setCode(ResponseCode.ENROLLMENTS_ALREADY_CLOSED).build());
            responseObserver.onCompleted();
            return;
        }
        if(classe.getClassState().getEnrolledStudents().containsKey(request.getStudent().getStudentId())) {
            responseObserver.onNext(EnrollResponse.newBuilder().setCode(ResponseCode.STUDENT_ALREADY_ENROLLED).build());
            responseObserver.onCompleted();
            return;
        }
        if(classe.getClassState().getCapacity() == classe.getClassState().getEnrolledStudents().size()) {
            responseObserver.onNext(EnrollResponse.newBuilder().setCode(ResponseCode.FULL_CLASS).build());
            responseObserver.onCompleted();
            return;
        }
        if(classe.getClassState().getDiscardedStudents().containsKey(request.getStudent().getStudentId())) {
            classe.getClassState().removeFromDiscarded(request.getStudent());
        }
        classe.enroll(request.getStudent().getStudentId(),request.getStudent().getStudentName());
        responseObserver.onNext(EnrollResponse.newBuilder().setCode(ResponseCode.OK).build());
        responseObserver.onCompleted();
    }
    

}