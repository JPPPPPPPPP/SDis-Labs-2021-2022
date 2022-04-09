package pt.ulisboa.tecnico.classes;

import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions;
import pt.ulisboa.tecnico.classes.contract.naming.ClassServerNamingServer;
import pt.ulisboa.tecnico.classes.contract.naming.NamingServerServiceGrpc;
import pt.ulisboa.tecnico.classes.namingserver.NamingServer;

import java.util.List;

public class NamingFrontend {

    public static List<ClassesDefinitions.ServerEntryState> Lookup(NamingServerServiceGrpc.NamingServerServiceBlockingStub stub, String servico, List<String> qualificadores) {
        ClassServerNamingServer.LookupResponse response = stub.lookup(ClassServerNamingServer.LookupRequest.newBuilder().
                setNomeServico(servico).addAllQualificadores(qualificadores).build());
        return response.getServidoresList();
    }
}
