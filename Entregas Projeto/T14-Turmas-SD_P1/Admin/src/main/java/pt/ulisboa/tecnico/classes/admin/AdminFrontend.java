package pt.ulisboa.tecnico.classes.admin;

import io.grpc.ManagedChannel;
import pt.ulisboa.tecnico.classes.Stringify;
import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer;
import pt.ulisboa.tecnico.classes.contract.admin.AdminServiceGrpc;

public class AdminFrontend {

    AdminServiceGrpc.AdminServiceBlockingStub stub;

    public AdminFrontend(ManagedChannel channel){
        stub = AdminServiceGrpc.newBlockingStub(channel);
    }

    public void Dump() {
        AdminClassServer.DumpRequest request = AdminClassServer.DumpRequest.newBuilder().build();
        AdminClassServer.DumpResponse response = stub.dump(request);
        System.out.println(Stringify.format(response.getClassState()));
    }
}
