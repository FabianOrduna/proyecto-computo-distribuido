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
import seguridad.ManejadorLlaves;

public class Server extends UnicastRemoteObject implements Hello {
    
    private ModeloAlumno modeloAlumno;
    private ModeloVuelos modeloVuelos;
    private ManejadorLlaves manejadorLlaves;
    
    public Server() throws RemoteException, SQLException{
        this.modeloAlumno = new ModeloAlumno();
        this.modeloVuelos = new ModeloVuelos();
    }
    
    public byte[] converterByte(String objeto) throws RemoteException, SQLException, IOException{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(objeto);
        byte[] cleartext = out.toByteArray();
        return cleartext;
    }
    
    public byte[] converterByte(int objeto) throws RemoteException, SQLException, IOException{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(objeto);
        byte[] cleartext = out.toByteArray();
        return cleartext;
    }
    
    public byte[] converterByte(ArrayList<Vuelo> objeto) throws RemoteException, SQLException, IOException{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(objeto);
        byte[] cleartext = out.toByteArray();
        return cleartext;
    }
    
    public byte[] converterByte(Vuelo objeto) throws RemoteException, SQLException, IOException{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(objeto);
        byte[] cleartext = out.toByteArray();
        return cleartext;
    }
    
    // Fabian: ayuda :( tiene un arraylist al igual que ArrayList<Vuelo> y nos marca "have the same erasure": 
    public byte[] converterByte(ArrayList<Persona> objeto, int i) throws RemoteException, SQLException, IOException{ 
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(objeto);
        byte[] cleartext = out.toByteArray();
        return cleartext;
    }
    
    // Fabian: ayuda :( tiene un arraylist al igual que ArrayList<Vuelo> y nos marca "have the same erasure": 
    public byte[] converterByte(ArrayList<Lugar> objeto, boolean i) throws RemoteException, SQLException, IOException{ 
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(objeto);
        byte[] cleartext = out.toByteArray();
        return cleartext;
    }
    
    @Override
    public int crearLlave() throws RemoteException{
        return manejadorLlaves.crearLlave();
    }
    
    public byte[] obtenerLlave(int llaveId) throws RemoteException{
        return manejadorLlaves.obtenerLlave(llaveId);
    }
    
    @Override
    public void coordLlave(int llaveId, byte[] clientPubKeyEnc) throws RemoteException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException{
        llaveServidor.coordinaConCliente(clientPubKeyEnc);
    }   
    
    @Override
    public byte[] sayHello(String persona, int llaveId, byte[] clientPubKeyEnc) throws RemoteException, SQLException, IOException{
        System.out.println("Hora de peticion: "+LocalDateTime.now().toString()+" : "+persona);
        String tmp = new String("Hello, world! " + persona);
        return converterByte(tmp);
    }
    
    @Override
    public byte[] sayHello(int llaveId, byte[] clientPubKeyEnc) throws RemoteException, SQLException, IOException{
        System.out.println("Hora de peticion: "+LocalDateTime.now().toString());
        String tmp = new String("Hello, world! ");
        return converterByte(tmp);
    }
    
    @Override
    public byte[] insertaAlumno(String nombre, String paterno, String materno, int llaveId, byte[] clientPubKeyEnc) throws RemoteException, IOException {
        try {
            return converterByte(modeloAlumno.insertaAlumno(nombre, paterno, materno));
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public byte[] actualizaAlumno(int idAlumno, String nombre, String paterno, String materno, int llaveId, byte[] clientPubKeyEnc) throws RemoteException, IOException {
        try {
            return converterByte(modeloAlumno.actualizaAlumno(idAlumno,nombre, paterno, materno));
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    @Override
    public byte[] vuelosHistoricos(int llaveId, byte[] clientPubKeyEnc) throws RemoteException, SQLException, IOException {
        return converterByte(modeloVuelos.vuelosHistoricos());
    }

    @Override
    public byte[] vuelosDisponibles(String fecha, int llaveId, byte[] clientPubKeyEnc) throws RemoteException, IOException {
        try {
            return converterByte(modeloVuelos.vuelosDisponibles(fecha));
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public byte[] obtenerVuelo(int idVuelo, int llaveId, byte[] clientPubKeyEnc) throws RemoteException, IOException{
        try {
            return converterByte(modeloVuelos.obtenVuelo(idVuelo));
        } catch (SQLException ex) {
            System.out.println(ex.toString());
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public byte[] obtenerPersonasVuelo(int idVuelo, int llaveId, byte[] clientPubKeyEnc) throws SQLException, IOException {
        return converterByte(modeloVuelos.obtenerPersonasVuelo(idVuelo), 1);
    }

    @Override
    public byte[] vuelosHistoricosPersona(int idPersona, int llaveId, byte[] clientPubKeyEnc) throws RemoteException, IOException {
        try {
            return converterByte(modeloVuelos.vuelosHistoricosPersona(idPersona));
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        
    }

    @Override
    public byte[] vuelosDisponiblesPersona(String fecha, int idPersona, int llaveId, byte[] clientPubKeyEnc) throws RemoteException, SQLException, IOException {
        return converterByte(this.modeloVuelos.vuelosDisponiblesPersona(fecha, idPersona));
    }

    @Override
    public byte[] vuelosAnterioresPersona(String fecha, int idPersona, int llaveId, byte[] clientPubKeyEnc) throws RemoteException, SQLException, IOException {
        return converterByte(modeloVuelos.vuelosAnterioresPersona(fecha, idPersona));
    }

    @Override
    public byte[] obtenerLugares(int llaveId, byte[] clientPubKeyEnc) throws RemoteException, IOException {
        try {
            return converterByte(modeloVuelos.obtenerLugares(), true);
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
//    public byte[] enviarPrueba() throws RemoteException, IOException, IllegalBlockSizeException, BadPaddingException {
//        Persona p = new Persona(1,"Fabian");
//        
//        System.out.println(p.toString());
//        
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        ObjectOutputStream os = new ObjectOutputStream(out);
//        os.writeObject(p);
//        
//        byte[] cleartext = out.toByteArray();
//        byte[] ciphertext = llaveServidor.encriptaMensaje(cleartext);
//        
//        return ciphertext;
//    }

    @Override
    public byte[] vuelosOrigenDestino(int idOrigen, int idDestino, int llaveId, byte[] clientPubKeyEnc) throws RemoteException, SQLException, IOException {
        return converterByte(this.modeloVuelos.vuelosOrigenDestino(idOrigen, idDestino));
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
