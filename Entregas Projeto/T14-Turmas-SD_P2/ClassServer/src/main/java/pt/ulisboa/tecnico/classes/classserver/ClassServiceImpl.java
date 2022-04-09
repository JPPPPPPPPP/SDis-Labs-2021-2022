package pt.ulisboa.tecnico.classes.classserver;

import io.grpc.stub.StreamObserver;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions;
import pt.ulisboa.tecnico.classes.contract.classserver.ClassServerServiceGrpc;
import pt.ulisboa.tecnico.classes.contract.classserver.ClassServerClassServer.*;


public class ClassServiceImpl extends ClassServerServiceGrpc.ClassServerServiceImplBase {
    private Classe classe;

    public ClassServiceImpl(Classe c) {
        this.classe = c;
    }

    @Override
    public void propagateState(PropagateStateRequest request, StreamObserver<PropagateStateResponse> responseObserver) {
        ClassState state = new ClassState();
        state.setCapacity(request.getClassState().getCapacity());
        state.setOpenEnrollments(request.getClassState().getOpenEnrollments());
        state.setEnrolled(request.getClassState().getEnrolledList());
        state.setDiscarded(request.getClassState().getDiscardedList());
        classe.setState(state);

        responseObserver.onNext(PropagateStateResponse.newBuilder().setCode(ClassesDefinitions.ResponseCode.OK).build());
        responseObserver.onCompleted();
    }
}
