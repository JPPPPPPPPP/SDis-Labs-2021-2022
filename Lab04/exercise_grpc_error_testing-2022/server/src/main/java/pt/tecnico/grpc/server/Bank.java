package pt.tecnico.grpc.server;

import pt.tecnico.grpc.Banking.RegisterResponse;
import pt.tecnico.grpc.Banking.ConsultResponse;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;



public class Bank {
    private static final Logger LOGGER = Logger.getLogger(Bank.class.getName());
    ConcurrentHashMap<String, Integer> clients = new ConcurrentHashMap<>();

    public void register(String client, Integer balance) {
        clients.put(client, balance);
        LOGGER.info("User: " + client + " has been instantiated with balance: " + balance);
    }

    public int getBalance(String client) {
        int balance = clients.get(client);
        return balance;
    }

    public boolean isClient(String client) {
        boolean exist = clients.containsKey(client);
        return exist;
    }
}
