package pt.ulisboa.tecnico.classes.namingserver;

import io.grpc.stub.StreamObserver;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions.*;
import pt.ulisboa.tecnico.classes.contract.naming.ClassServerNamingServer.*;
import pt.ulisboa.tecnico.classes.contract.naming.NamingServerServiceGrpc;

import java.util.ArrayList;
import java.util.List;

public class NamingServerServiceImpl extends NamingServerServiceGrpc.NamingServerServiceImplBase {
    private Naming naming;

    public NamingServerServiceImpl(Naming n) {
        this.naming = n;
    }

    @Override
    public void register(RegisterRequest request, StreamObserver<RegisterResponse> responseObserver) {
        if (naming.checkPort(request.getPort())) {
            System.err.printf("Port already used");
            return;
        }
        naming.register(request.getNomeServico(), request.getHost(), request.getPort(), request.getQualificadoresList());

        responseObserver.onNext(RegisterResponse.newBuilder().setCode(ResponseCode.OK).build());
        responseObserver.onCompleted();
    }

    @Override
    public void lookup(LookupRequest request, StreamObserver<LookupResponse> responseObserver) {
        List<ServerEntryState> serverEntryStates = new ArrayList<>();
        if (request.getQualificadoresList().size() > 0) {
            List<ServerEntry> serverEntries = naming.lookup(request.getNomeServico(), request.getQualificadoresList());

            for(ServerEntry s : serverEntries) {
                serverEntryStates.add(s.asMessage());
            }
        }

        else {
            List<String> qualifiers = new ArrayList<>();
            qualifiers.add("P");

            List<ServerEntry> serverEntriesP = naming.lookup(request.getNomeServico(), qualifiers);

            for(ServerEntry s : serverEntriesP) {
                serverEntryStates.add(s.asMessage());
            }

            qualifiers.remove("P");
            qualifiers.add("S");

            List<ServerEntry> serverEntriesS = naming.lookup(request.getNomeServico(), qualifiers);

            for(ServerEntry s : serverEntriesS) {
                serverEntryStates.add(s.asMessage());
            }
        }

        responseObserver.onNext(LookupResponse.newBuilder().setCode(ResponseCode.OK).addAllServidores(serverEntryStates).build());
        responseObserver.onCompleted();
    }

    @Override
    public void delete(DeleteRequest request, StreamObserver<DeleteResponse> responseObserver) {
        naming.delete(request.getNomeServico(), request.getHost(), request.getPort());

        responseObserver.onNext(DeleteResponse.newBuilder().setCode(ResponseCode.OK).build());
        responseObserver.onCompleted();
    }
}