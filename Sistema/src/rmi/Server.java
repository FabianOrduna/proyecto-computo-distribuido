package rmi;
        
import bd.ModeloAlumno;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbcp2.BasicDataSource;
        
public class Server extends UnicastRemoteObject implements Hello {
    
    private ModeloAlumno modeloAlumno;
    
    public Server() throws RemoteException, SQLException{
        this.modeloAlumno = new ModeloAlumno();
    }

    @Override
    public String sayHello(String persona){
        System.out.println("Hora de peticion: "+LocalDateTime.now().toString()+" : "+persona);
        return "Hello, world! "+persona;
    }
    
    @Override
    public String sayHello(){
        System.out.println("Hora de peticion: "+LocalDateTime.now().toString());
        return "Hello, world! ";
    }
    
    @Override
    public int insertaAlumno(String nombre, String paterno, String materno) throws RemoteException {
        try {
            return modeloAlumno.insertaAlumno(nombre, paterno, materno);
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    @Override
    public int actualizaAlumno(int idAlumno, String nombre, String paterno, String materno) throws RemoteException {
        try {
            return modeloAlumno.actualizaAlumno(idAlumno,nombre, paterno, materno);
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
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