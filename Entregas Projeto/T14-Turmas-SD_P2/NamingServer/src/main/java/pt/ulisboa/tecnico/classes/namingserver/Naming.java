package pt.ulisboa.tecnico.classes.namingserver;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Naming {
    private static final Logger LOGGER = Logger.getLogger(Class.class.getName());

    NamingServices namingServices = new NamingServices();

    public void register(String servico, String host, String port, List<String> qualificadores) {
        ServerEntry serverEntry = new ServerEntry(host, port, qualificadores);
        namingServices.addNamingServicesEntry(servico,serverEntry);
    }

    public List<ServerEntry> lookup(String servico, List<String> qualificadores) {
        List<ServerEntry> ret = new ArrayList<>();
        for(ServerEntry s : namingServices.getServiceEntries(servico).getServerEntries()) {
            if (s.getQualificadores().containsAll(qualificadores)) {
                ret.add(s);
            }
        }
        return ret;
    }

    public void delete (String servico, String host, String port) {
        namingServices.removeServerEntry(servico, port);
    }

    public boolean checkPort (String port) {
        return namingServices.checkPort(port);
    }
}
