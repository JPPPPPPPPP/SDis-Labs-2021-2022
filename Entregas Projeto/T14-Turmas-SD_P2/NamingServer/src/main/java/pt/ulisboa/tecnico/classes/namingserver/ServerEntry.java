package pt.ulisboa.tecnico.classes.namingserver;

import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions;

import java.util.ArrayList;
import java.util.List;

public class ServerEntry {

    String host;
    String port;
    List<String> qualificadores;

    public ServerEntry(String h, String p, List<String> qual) {
        this.host = h;
        this.port = p;
        this.qualificadores = qual;
    }

    public String getHost() {
        return this.host;
    }

    public String getPort() {
        return this.port;
    }

    public List<String> getQualificadores() {
        return this.qualificadores;
    }

    public ClassesDefinitions.ServerEntryState asMessage () {
        return ClassesDefinitions.ServerEntryState.newBuilder()
                .setHost(host)
                .setPort(port)
                .addAllQualificadores(qualificadores)
                .build();
    }
}
