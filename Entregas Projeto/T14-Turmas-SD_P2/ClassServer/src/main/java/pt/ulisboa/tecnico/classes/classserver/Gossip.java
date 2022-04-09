package pt.ulisboa.tecnico.classes.classserver;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import pt.ulisboa.tecnico.classes.NamingFrontend;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions;
import pt.ulisboa.tecnico.classes.contract.classserver.ClassServerClassServer;
import pt.ulisboa.tecnico.classes.contract.classserver.ClassServerServiceGrpc;
import pt.ulisboa.tecnico.classes.contract.naming.NamingServerServiceGrpc;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class Gossip extends TimerTask {

    NamingServerServiceGrpc.NamingServerServiceBlockingStub stub;
    Classe classe;

    Gossip(NamingServerServiceGrpc.NamingServerServiceBlockingStub stubb, Classe c) {
        this.stub = stubb;
        this.classe = c;
    }

    public void run() {
        ClassServerServiceGrpc.ClassServerServiceBlockingStub ligacao;
        List<String> qualifiers = new ArrayList<>();
        qualifiers.add("S");
        List<ClassesDefinitions.ServerEntryState> lookupResponse = NamingFrontend.Lookup(stub, "Turmas", qualifiers);
        for (ClassesDefinitions.ServerEntryState s : lookupResponse) {
            ManagedChannel channel = ManagedChannelBuilder.forAddress(s.getHost(), Integer.parseInt(s.getPort())).usePlaintext().build();
            ligacao = ClassServerServiceGrpc.newBlockingStub(channel);
            ClassServerClassServer.PropagateStateRequest request =
                    ClassServerClassServer.PropagateStateRequest.newBuilder()
                            .setClassState(classe.getClassState().asMessage()).build();
            ClassServerClassServer.PropagateStateResponse gossipResponse = ligacao.propagateState(request);
        }
    }
}
