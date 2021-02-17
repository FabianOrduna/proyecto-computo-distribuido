package rmi;
        
import bd.Lugar;
import bd.ModeloAlumno;
import bd.ModeloVuelos;
import bd.Persona;
import bd.Vuelo;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import seguridad.LlaveServidor;
        
public class ServerUnServer extends UnicastRemoteObject implements Hello {
    
    private ModeloAlumno modeloAlumno;
    private ModeloVuelos modeloVuelos;
    private LlaveServidor llaveServidor;
    
    public ServerUnServer() throws RemoteException, SQLException{
        this.modeloAlumno = new ModeloAlumno();
        this.modeloVuelos = new ModeloVuelos();
    }
    
    @Override
    public byte[] crearLlave(int llaveId) throws RemoteException{
        try {
            this.llaveServidor = new LlaveServidor(llaveId);
            // Alice encodes her public key, and sends it over to Bob.
            byte[] alicePubKeyEnc = llaveServidor.obtenLlaveInicial();
            return alicePubKeyEnc;
        }catch(Exception e){
            return null;
        }   
    }
    
    @Override
    public byte[] obtenParametrosDeCifrado() throws IOException {
        return llaveServidor.obtenParametrosDeCifrado();
    }
    
    @Override
    public void coordLlave(byte[] bobPubKeyEnc) throws RemoteException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException{
        llaveServidor.coordinaConCliente(bobPubKeyEnc);
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
        return modeloVuelos.vuelosHistoricos();
    }

    @Override
    public ArrayList<Vuelo> vuelosDisponibles(String fecha) throws RemoteException {
        try {
            return modeloVuelos.vuelosDisponibles(fecha);
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Vuelo obtenerVuelo(int idVuelo) throws RemoteException{
        try {
            return modeloVuelos.obtenVuelo(idVuelo);
        } catch (SQLException ex) {
            System.out.println(ex.toString());
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public ArrayList<Persona> obtenerPersonasVuelo(int idVuelo) {
        return modeloVuelos.obtenerPersonasVuelo(idVuelo);
    }

    @Override
    public ArrayList<Vuelo> vuelosHistoricosPersona(int idPersona) throws RemoteException {
        try {
            return modeloVuelos.vuelosHistoricosPersona(idPersona);
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        
    }

    @Override
    public ArrayList<Vuelo> vuelosDisponiblesPersona(String fecha, int idPersona) throws RemoteException {
        return this.modeloVuelos.vuelosDisponiblesPersona(fecha, idPersona);
    }

    @Override
    public ArrayList<Vuelo> vuelosAnterioresPersona(String fecha, int idPersona) throws RemoteException {
        return modeloVuelos.vuelosAnterioresPersona(fecha, idPersona);
    }

    @Override
    public ArrayList<Lugar> obtenerLugares() throws RemoteException {
        try {
            return modeloVuelos.obtenerLugares();
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public byte[] enviarPrueba() throws RemoteException, IOException, IllegalBlockSizeException, BadPaddingException {
        Persona p = new Persona(1,"Fabian");
        
        System.out.println(p.toString());
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(p);
        
        byte[] cleartext = out.toByteArray();
        byte[] ciphertext = llaveServidor.encriptaMensaje(cleartext);
        
        return ciphertext;
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
