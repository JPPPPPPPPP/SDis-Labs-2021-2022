package pt.ulisboa.tecnico.classes.classserver;

import io.grpc.stub.StreamObserver;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions.*;
import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer.*;
import pt.ulisboa.tecnico.classes.contract.admin.AdminServiceGrpc;

public class AdminServiceImpl extends AdminServiceGrpc.AdminServiceImplBase {
    Classe classe;

    public AdminServiceImpl(Classe c) {
        this.classe = c;
    }

    @Override
    public void dump(DumpRequest request, StreamObserver responseObserver) {
        ClassState classState = classe.getClassState();
        responseObserver.onNext(DumpResponse.newBuilder().setCode(ResponseCode.OK).
                setClassState(classState.asMessage()).build());
        if (classe.getMode()) {
            System.err.printf("Received a dump request from Admin\n\n");
        }
        responseObserver.onCompleted();
    }
    @Override
    public void activate(ActivateRequest request, StreamObserver responseObserver) {
        classe.activate();
        responseObserver.onNext(ActivateResponse.newBuilder().setCode(ResponseCode.OK).build());
        responseObserver.onCompleted();
    }


    @Override
    public void deactivate(DeactivateRequest request, StreamObserver responseObserver) {
        classe.deactivate();
        responseObserver.onNext(DeactivateResponse.newBuilder().setCode(ResponseCode.OK).build());
        responseObserver.onCompleted();
    }
}
