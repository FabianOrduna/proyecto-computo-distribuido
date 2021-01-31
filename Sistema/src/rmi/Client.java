package rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    private Client() {}

    public static void main(String[] args) {

        String host = (args.length < 1) ? "148.205.36.206" : args[0];
        try {
            System.setProperty("java.rmi.server.hostname", host);
            //Registry registry = LocateRegistry.getRegistry(1010);
            Registry registry = LocateRegistry.getRegistry(host, 1010);
            Hello stub = (Hello) registry.lookup("Hello");
            String response = stub.sayHello("Fabián");
            System.out.println("response: " + response);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}