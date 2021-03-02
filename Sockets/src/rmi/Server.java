package rmi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;     
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import seguridad.ManejadorLlaves;

public class Server extends UnicastRemoteObject implements Hello {
    
    private ManejadorLlaves manejadorLlaves;
    private static int x;
    private static int y;
    
    public Server() throws RemoteException, SQLException{

        this.manejadorLlaves = new ManejadorLlaves();
        this.x = 0;
        this.y = 0;
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
    public byte[] obtenParametrosDeCifrado(int llaveId, byte[] clientPubKeyEnc) throws IOException{
        return manejadorLlaves.obtenParametrosDeCifrado(llaveId);
    }    
    
    @Override
    public void sumaX(byte[] idVuelo, int llaveId, byte[] clientPubKeyEnc, byte[] paramsEncriptClient) throws RemoteException, SQLException, IOException {
        System.out.println("Operacion de suma recibida en el servidor");
        int tmpIdVuelo = 0;
        System.out.println("Parametro de entrada: ");
        System.out.println(idVuelo);
        try {
            tmpIdVuelo = ByteBuffer.wrap(this.manejadorLlaves.desencripta(clientPubKeyEnc, idVuelo, paramsEncriptClient)).getInt();
            System.out.println("Número a sumar a X:"+tmpIdVuelo);
            System.out.println("Fin de desencripcion");
            this.x += tmpIdVuelo;
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }
    
    @Override
    public void sumaY(byte[] idVuelo, int llaveId, byte[] clientPubKeyEnc, byte[] paramsEncriptClient) throws RemoteException, SQLException, IOException {
        System.out.println("Operacion de suma recibida en el servidor");
        int tmpIdVuelo = 0;
        System.out.println("Parametro de entrada: ");
        System.out.println(idVuelo);
        try {
            tmpIdVuelo = ByteBuffer.wrap(this.manejadorLlaves.desencripta(clientPubKeyEnc, idVuelo, paramsEncriptClient)).getInt();
            System.out.println("Número a sumar a Y:"+tmpIdVuelo);
            System.out.println("Fin de desencripcion");
            this.y += tmpIdVuelo;
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    @Override
    public byte[] getX(byte[] clientPubKeyEnc){
        byte[] tmp = ByteBuffer.allocate(4).putInt(this.x).array();
        try {
            return this.manejadorLlaves.encripta(clientPubKeyEnc, tmp);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return null;
    }

     @Override
    public byte[] getY(byte[] clientPubKeyEnc){
        byte[] tmp = ByteBuffer.allocate(4).putInt(this.y).array();
        try {
            return this.manejadorLlaves.encripta(clientPubKeyEnc, tmp);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return null;
    }

      

    @Override
    public void multiplicaX(byte[] idVuelo, int idLlave, byte[] clientPubKeyEnc, byte[] paramsEncriptClient) {
        System.out.println("Operacion de suma recibida en el servidor");
        int tmpIdVuelo = 0;
        System.out.println("Parametro de entrada: ");
        System.out.println(idVuelo);
        try {
            tmpIdVuelo = ByteBuffer.wrap(this.manejadorLlaves.desencripta(clientPubKeyEnc, idVuelo, paramsEncriptClient)).getInt();
            System.out.println("Número a sumar a Y:"+tmpIdVuelo);
            System.out.println("Fin de desencripcion");
            this.y *= tmpIdVuelo;
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    @Override
    public void multiplicaY(byte[] idVuelo, int idLlave, byte[] clientPubKeyEnc, byte[] paramsEncriptClient) {
        System.out.println("Operacion de suma recibida en el servidor");
        int tmpIdVuelo = 0;
        System.out.println("Parametro de entrada: ");
        System.out.println(idVuelo);
        try {
            tmpIdVuelo = ByteBuffer.wrap(this.manejadorLlaves.desencripta(clientPubKeyEnc, idVuelo, paramsEncriptClient)).getInt();
            System.out.println("Número a sumar a Y:"+tmpIdVuelo);
            System.out.println("Fin de desencripcion");
            this.y *= tmpIdVuelo;
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    public static void main(String args[]) {
        try {
            Server obj = new Server();
            Registry registry = LocateRegistry.createRegistry(1010);
            registry.bind("Hello", obj);
            System.out.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception no funcionó: " + e.toString());
            e.printStackTrace();
        }
    } 
    
    
    
}
