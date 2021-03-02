package rmi;
        
import bd.Lugar;
import bd.ModeloAlumno;
import bd.ModeloVuelos;
import bd.Persona;
import bd.Vuelo;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
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
        this.manejadorLlaves = new ManejadorLlaves();
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
    /*public byte[] converterByte(ArrayList<Persona> objeto, int i) throws RemoteException, SQLException, IOException{ 
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(objeto);
        byte[] cleartext = out.toByteArray();
        return cleartext;
    }*/
    
    // Fabian: ayuda :( tiene un arraylist al igual que ArrayList<Vuelo> y nos marca "have the same erasure": 
    public byte[] converterByte(ArrayList<Lugar> objeto, boolean i) { 
        try{
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(objeto);
        byte[] cleartext = out.toByteArray();
        return cleartext;
        }catch(Exception e){
            System.out.println(e.toString());
            return null;
        }
    }
    
    public byte[] serializa(Object objeto) { 
        try{
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(objeto);
        byte[] cleartext = out.toByteArray();
        return cleartext;
        }catch(Exception e){
            System.out.println(e.toString());
            return null;
        }
    }
    
    @Override
    public int crearLlave() throws RemoteException{
        //System.out.println("Creando llave");
        return manejadorLlaves.crearLlave();
    }
    
    @Override
    public byte[] obtenLlave(int llaveId) throws RemoteException{
        //System.out.println("Regresando llave");
        
        return manejadorLlaves.obtenerLlave(llaveId);
    }
    
    @Override
    public void coordLlave(int llaveId, byte[] clientPubKeyEnc) throws RemoteException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException{
        manejadorLlaves.coordLlave(llaveId,clientPubKeyEnc);
        //manejadorLlaves.imprimeSecreto(int llaveId);
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
        try {
            /* metodo listo*/
            ArrayList<Vuelo> l = modeloVuelos.vuelosHistoricos();
            //System.out.println(l.toString());
            byte [] resParcial = serializa(l);
            byte [] encriptado = this.manejadorLlaves.encripta(clientPubKeyEnc, resParcial);
            System.out.println("Vuelos encriptados históricos");
            return encriptado;
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return null;
    }

    @Override
    public byte[] vuelosDisponibles(String fecha, int llaveId, byte[] clientPubKeyEnc) throws RemoteException, IOException {
        
        try {
            /* metodo listo*/
            ArrayList<Vuelo> l = modeloVuelos.vuelosDisponibles(fecha);
            //System.out.println(l.toString());
            byte [] resParcial = serializa(l);
            byte [] encriptado = this.manejadorLlaves.encripta(clientPubKeyEnc, resParcial);
            System.out.println("Vuelos encriptados");
            return encriptado;
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return null;     
    }

    @Override
    public byte[] obtenerVuelo(int idVuelo, int llaveId, byte[] clientPubKeyEnc) throws RemoteException, IOException{
        try {
            
            Vuelo l = modeloVuelos.obtenVuelo(idVuelo);
            //System.out.println(l.toString());
            byte [] resParcial = converterByte(l);
            byte [] encriptado = this.manejadorLlaves.encripta(clientPubKeyEnc, resParcial);
            System.out.println("Lugares encriptados");
            return encriptado;
        } catch (SQLException ex) {
            System.out.println(ex.toString());
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public byte[] obtenParametrosDeCifrado(int llaveId, byte[] clientPubKeyEnc) throws IOException{
        return manejadorLlaves.obtenParametrosDeCifrado(llaveId);
    }
    
    
    
    @Override
    public byte[] obtenerPersonasVuelo(int idVuelo, int llaveId, byte[] clientPubKeyEnc) throws SQLException, IOException {
        
        try {
            /* metodo listo*/
            ArrayList<Persona> l = modeloVuelos.obtenerPersonasVuelo(idVuelo);
            //System.out.println(l.toString());
            byte [] resParcial = serializa(l);
            byte [] encriptado = this.manejadorLlaves.encripta(clientPubKeyEnc, resParcial);
            System.out.println("Personas encriptados");
            return encriptado;
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return null;
        
    }

    @Override
    public byte[] vuelosHistoricosPersona(int idPersona, int llaveId, byte[] clientPubKeyEnc) throws RemoteException, IOException {
        
        try {
            /* metodo listo*/
            ArrayList<Vuelo> l = modeloVuelos.vuelosHistoricosPersona(idPersona);
            //System.out.println(l.toString());
            byte [] resParcial = serializa(l);
            byte [] encriptado = this.manejadorLlaves.encripta(clientPubKeyEnc, resParcial);
            System.out.println("Vuelos históricos por persona");
            return encriptado;
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return null;
                
    }

    @Override
    public byte[] vuelosDisponiblesPersona(String fecha, int idPersona, int llaveId, byte[] clientPubKeyEnc) throws RemoteException, SQLException, IOException {
        
        try {
            /* metodo listo*/
            ArrayList<Vuelo> l = this.modeloVuelos.vuelosDisponiblesPersona(fecha, idPersona);
            //System.out.println(l.toString());
            byte [] resParcial = serializa(l);
            byte [] encriptado = this.manejadorLlaves.encripta(clientPubKeyEnc, resParcial);
            System.out.println("Vuelos encriptados por persona");
            return encriptado;
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return null;
 
    }

    @Override
    public byte[] vuelosAnterioresPersona(String fecha, int idPersona, int llaveId, byte[] clientPubKeyEnc) throws RemoteException, SQLException, IOException {
        try {
            /* metodo listo*/
            ArrayList<Vuelo> l = modeloVuelos.vuelosAnterioresPersona(fecha, idPersona);
            //System.out.println(l.toString());
            byte [] resParcial = serializa(l);
            byte [] encriptado = this.manejadorLlaves.encripta(clientPubKeyEnc, resParcial);
            System.out.println("Vuelos persona encriptados");
            return encriptado;
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return null;
        
    }

    @Override
    public byte[] obtenerLugares(int llaveId, byte[] clientPubKeyEnc) throws RemoteException, IOException {
        try {
            /* metodo listo*/
            ArrayList<Lugar> l = modeloVuelos.obtenerLugares();
            //System.out.println(l.toString());
            byte [] resParcial = converterByte(l,true);
            byte [] encriptado = this.manejadorLlaves.encripta(clientPubKeyEnc, resParcial);
            System.out.println("Lugares encriptados");
            return encriptado;
        } catch (Exception ex) {
            System.out.println(ex.toString());
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
       
        try {
            /* metodo listo*/
            ArrayList<Vuelo> l = this.modeloVuelos.vuelosOrigenDestino(idOrigen, idDestino);
            //System.out.println(l.toString());
            byte [] resParcial = serializa(l);
            byte [] encriptado = this.manejadorLlaves.encripta(clientPubKeyEnc, resParcial);
            System.out.println("Vuelos Origen Destino");
            return encriptado;
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return null;
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

    //para los parámetros mandados por el cliente
    public byte[] obtenerPersonasVuelo(byte[] idVuelo, int llaveId, byte[] clientPubKeyEnc, byte[] paramsEncriptClient) throws RemoteException, SQLException, IOException {
        int tmpIdVuelo = 0;
        System.out.println("Parametro de entrada: ");
        System.out.println(idVuelo);
        try {
            tmpIdVuelo = ByteBuffer.wrap(this.manejadorLlaves.desencripta(clientPubKeyEnc, idVuelo, paramsEncriptClient)).getInt();
            System.out.println("IdVuelo-Fof "+tmpIdVuelo);
            System.out.println("Fin de desencripcion");
            //aqui ya se busca el metodo
            ArrayList l = modeloVuelos.obtenerPersonasVuelo(tmpIdVuelo);
            System.out.println(l.toString());
            
            //*************************************************
            byte [] resParcial = serializa(l);
            System.out.println("Despues de serializar");
            byte [] encriptado = this.manejadorLlaves.encripta(clientPubKeyEnc, resParcial);
            System.out.println("Desués del enctiptado");
            //*************************************************

            return encriptado;
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return null;
    }

    @Override
    public byte[] obtenerPersonasVuelo(byte[] idVuelo, int llaveId, byte[] clientPubKeyEnc) throws RemoteException, SQLException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public byte[] vuelosAnterioresPersona(byte[] fecha, byte[] idPersona, int llaveId, byte[] clientPubKeyEnc, byte[] paramsEncriptClient) throws RemoteException, SQLException, IOException {
        int tmpIdVuelo = 1;
        String tmpFecha = "2020-01-01";
        System.out.println("");
        try {
            tmpIdVuelo = ByteBuffer.wrap(this.manejadorLlaves.desencripta(clientPubKeyEnc, idPersona, paramsEncriptClient)).getInt();
            System.out.println("Vuelo descifrado: "+tmpIdVuelo);
            
            tmpFecha = new String(this.manejadorLlaves.desencripta(clientPubKeyEnc, fecha, paramsEncriptClient));
            //aqui ya se busca el metodo
            
            System.out.println("Fecha descifrada: "+tmpFecha);
            
            
            ArrayList l = modeloVuelos.vuelosAnterioresPersona(tmpFecha, tmpIdVuelo);
            System.out.println(l.toString());
            
            //*************************************************
            byte [] resParcial = serializa(l);
            System.out.println("Despues de serializar");
            byte [] encriptado = this.manejadorLlaves.encripta(clientPubKeyEnc, resParcial);
            System.out.println("Desués del enctiptado");
            //*************************************************

            return encriptado;
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return null;
        
    }

}
