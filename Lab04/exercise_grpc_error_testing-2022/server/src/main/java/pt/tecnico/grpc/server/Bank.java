package pt.tecnico.grpc.server;

import pt.tecnico.grpc.Banking.RegisterResponse;
import pt.tecnico.grpc.Banking.ConsultResponse;
import pt.tecnico.grpc.Banking.DepositResponse;

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

    public void deposit(String client, Integer amount) {
        clients.computeIfPresent(client, (key, oldValue) -> oldValue + amount);
        LOGGER.info("User: " + client + " has had amount: " + amount + " added to account");
    }

    public boolean isAmount(Integer amount) {
        return amount > 0;
    }
}
