package pt.ulisboa.tecnico.classes.namingserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServiceEntry {
    String nomeServico = "";
    List<ServerEntry> serverEntries = new ArrayList<>();

    public ServiceEntry(String nomeServ, ServerEntry servEntry) {
        this.nomeServico = nomeServ;
        addToServerEntries(servEntry);
    }

    public void addToServerEntries(ServerEntry servEntry) {
        this.serverEntries.add(servEntry);
    }

    public void removeFromServerEntries(ServerEntry servEntry) {
        this.serverEntries.remove(servEntry);
    }

    public String getNomeServico() {
        return nomeServico;
    }

    public List<ServerEntry> getServerEntries() {
        return serverEntries;
    }
}
