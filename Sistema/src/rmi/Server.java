package rmi;
        
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
        
public class Server extends UnicastRemoteObject implements Hello {
        
    public Server() throws RemoteException{}

    @Override
    public String sayHello(String persona){
        System.out.println("Hora de peticion: "+LocalDateTime.now().toString());
        return "Hello, world! "+persona;
    }
    
    @Override
    public String sayHello(){
        System.out.println("Hora de peticion: "+LocalDateTime.now().toString());
        return "Hello, world! ";
    }
    
    public static void main(String args[]) {
        
        try {
            Server obj = new Server();
            //Hello stub = (Hello) UnicastRemoteObject.exportObject(obj, 0);

            // Bind the remote object's stub in the registry
            //Registry registry = LocateRegistry.getRegistry();
            Registry registry = LocateRegistry.createRegistry(1010);
            registry.bind("Hello", obj);

            System.out.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}