package pt.ulisboa.tecnico.classes.namingserver;

import java.util.ArrayList;
import java.util.List;



public class NamingServices {
    //String: nome de servico
    List<ServiceEntry> serviceEntries = new ArrayList<>();

    public  NamingServices() {

    }

    public void addNamingServicesEntry (String service, ServerEntry serverEntry) {
        boolean exists =false;
        for (ServiceEntry s : serviceEntries) {
            if (s.getNomeServico().equals(service)) {
                s.addToServerEntries(serverEntry);
                exists = true;
            }
        }

        if (!exists) {
            ServiceEntry serviceEntry = new ServiceEntry(service, serverEntry);
            serviceEntries.add(serviceEntry);
        }
    }

    public ServiceEntry getServiceEntries (String service) {
        for (ServiceEntry s : serviceEntries) {
            if (s.getNomeServico().equals(service)) {
                return s;
            }
        }
        return null;
    }

    public void removeServerEntry (String service, String port) {
        for (ServiceEntry s : serviceEntries) {
            if (s.getNomeServico().equals(service)) {
                s.getServerEntries().removeIf(server -> server.getPort().equals(port));
            }
        }
    }

    public boolean checkPort (String port) {
        boolean ret = false;
        for (ServiceEntry s : serviceEntries) {
            for (ServerEntry server : s.getServerEntries()) {
                if (server.getPort().equals(port))
                    ret = true;
            }
        }
        return ret;
    }
}
