package rmi;

import bd.Vuelo;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class Client {

    private Client() {}

    public static void main(String[] args) {

        String host = (args.length < 1) ? "148.205.36.206" : args[0];
        try {
            System.setProperty("java.rmi.server.hostname", host);
            //Registry registry = LocateRegistry.getRegistry(1010);
            Registry registry = LocateRegistry.getRegistry(host, 1010);
            Hello stub = (Hello) registry.lookup("Hello");
            
            /*
            ArrayList<Vuelo> response = stub.vuelosOrigenDestino(3, 5);
            if(response!=null){
                System.out.println("response: " + response.toString());
            }else{
                System.out.println("La respuesta fue nula, no se encontró el vuelo.");
            }*/
            
            System.out.println("Aqui escribe el metodo que quieres probar");
            
            
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}