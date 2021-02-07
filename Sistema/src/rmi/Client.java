package rmi;

import bd.Vuelo;
import java.io.BufferedReader;
import java.io.InputStreamReader;
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
            //String response = stub.sayHello("FOF");
            //int response = stub.insertaAlumno("Juana Cecilia", "Ferreira", "Ascencio");
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); 
            // Reading data using readLine 
            System.out.println("Ingresa el numero de vuelo");
            int numVuelo = Integer.parseInt(reader.readLine());           
            Vuelo response = stub.obtenerVuelo(numVuelo);
            if(response!=null){
                System.out.println("response: " + response.toString());
            }else{
                System.out.println("La respuesta fue nula, no se encontró el vuelo.");
            }
            
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}