package pt.ulisboa.tecnico.classes.admin;

import com.sun.jdi.IntegerValue;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import pt.ulisboa.tecnico.classes.NamingFrontend;
import pt.ulisboa.tecnico.classes.Stringify;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions;
import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer;
import pt.ulisboa.tecnico.classes.contract.admin.AdminServiceGrpc;
import pt.ulisboa.tecnico.classes.contract.naming.NamingServerServiceGrpc;

import java.util.ArrayList;
import java.util.List;

public class AdminFrontend {

    NamingServerServiceGrpc.NamingServerServiceBlockingStub stub;

    public AdminFrontend(ManagedChannel channel){
        stub = NamingServerServiceGrpc.newBlockingStub(channel);
    }

    public void Dump(String token) {
        AdminServiceGrpc.AdminServiceBlockingStub ligacao;
        List<String> qualifiers = new ArrayList<>();
        qualifiers.add(token);
        List<ClassesDefinitions.ServerEntryState> response = NamingFrontend.Lookup(stub, "Turmas", qualifiers);
        for (ClassesDefinitions.ServerEntryState s : response) {
            ManagedChannel channel = ManagedChannelBuilder.forAddress(s.getHost(), Integer.parseInt(s.getPort())).usePlaintext().build();
            ligacao = AdminServiceGrpc.newBlockingStub(channel);
            AdminClassServer.DumpRequest request = AdminClassServer.DumpRequest.newBuilder().build();
            AdminClassServer.DumpResponse ret = ligacao.dump(request);
            System.out.println(Stringify.format(ret.getClassState()));
        }
    }

    public void Lookup(String servico, String qualificadores) {
        List<String> qualifiers = new ArrayList<>();
        qualifiers.add(qualificadores);
        List<ClassesDefinitions.ServerEntryState> response = NamingFrontend.Lookup(stub, servico, qualifiers);
        for (ClassesDefinitions.ServerEntryState s : response) {
            System.out.println(Stringify.format(s));
        }
    }
    public void activate(String qualificadores) {
        AdminServiceGrpc.AdminServiceBlockingStub ligacao;
        List<String> qualifiers = new ArrayList<>();
        qualifiers.add(qualificadores);
        List<ClassesDefinitions.ServerEntryState> response = NamingFrontend.Lookup(stub, "Turmas", qualifiers);
        for (ClassesDefinitions.ServerEntryState s : response) {
            ManagedChannel channel = ManagedChannelBuilder.forAddress(s.getHost(), Integer.parseInt(s.getPort())).usePlaintext().build();
            ligacao = AdminServiceGrpc.newBlockingStub(channel);
            AdminClassServer.ActivateRequest request = AdminClassServer.ActivateRequest.newBuilder().build();
            AdminClassServer.ActivateResponse res = ligacao.activate(request);
            System.out.println(Stringify.format(res.getCode()));
        }
    }

    public void deactivate(String qualificadores) {
        AdminServiceGrpc.AdminServiceBlockingStub ligacao;
        List<String> qualifiers = new ArrayList<>();
        qualifiers.add(qualificadores);
        List<ClassesDefinitions.ServerEntryState> response = NamingFrontend.Lookup(stub, "Turmas", qualifiers);
        for (ClassesDefinitions.ServerEntryState s : response) {
            ManagedChannel channel = ManagedChannelBuilder.forAddress(s.getHost(), Integer.parseInt(s.getPort())).usePlaintext().build();
            ligacao = AdminServiceGrpc.newBlockingStub(channel);
            AdminClassServer.DeactivateRequest request = AdminClassServer.DeactivateRequest.newBuilder().build();
            AdminClassServer.DeactivateResponse res = ligacao.deactivate(request);
            System.out.println(Stringify.format(res.getCode()));
        }

    }
}
