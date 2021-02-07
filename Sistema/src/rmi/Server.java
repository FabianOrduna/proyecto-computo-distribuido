package rmi;
        
import bd.Lugar;
import bd.ModeloAlumno;
import bd.ModeloVuelos;
import bd.Persona;
import bd.Vuelo;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbcp2.BasicDataSource;
        
public class Server extends UnicastRemoteObject implements Hello {
    
    private ModeloAlumno modeloAlumno;
    private ModeloVuelos modeloVuelos;
    
    public Server() throws RemoteException, SQLException{
        this.modeloAlumno = new ModeloAlumno();
        this.modeloVuelos = new ModeloVuelos();
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
    
    @Override
    public ArrayList<Vuelo> vuelosHistoricos() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Vuelo> vuelosDisponibles(String fecha) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Vuelo obtenerVuelo(int idVuelo) throws RemoteException{
        try {
            return modeloVuelos.obtenVuelo(idVuelo);
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public ArrayList<Persona> obtenerPersonasVuelo(int idVuelo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Vuelo> vuelosHistoricosPersona(int idPersona) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Vuelo> vuelosDisponiblesPersona(String fecha, int idPersona) throws RemoteException {
        return this.modeloVuelos.vuelosDisponiblesPersona(fecha, idPersona);
    }

    @Override
    public ArrayList<Vuelo> vuelosAnterioresPersona(String fecha, int idPersona) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Lugar> obtenerLugares() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Vuelo> vuelosOrigenDestino(int idOrigen, int idDestino) throws RemoteException {
        return this.modeloVuelos.vuelosOrigenDestino(idOrigen, idDestino);
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