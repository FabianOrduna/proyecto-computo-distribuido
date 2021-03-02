package rmi;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import javax.crypto.NoSuchPaddingException;
import seguridad.LlaveCliente;

public class Cliente {

    private Cliente() {}

    public static void main(String[] args) throws RemoteException, NotBoundException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException, InvalidKeyException, NoSuchPaddingException, IOException {

        String host = (args.length < 1) ? "localhost" : args[0];
        System.setProperty("java.rmi.server.hostname", host);
        Registry registry = LocateRegistry.getRegistry(host, 1010);
        Hello stub = (Hello) registry.lookup("Hello");
        
        int idLlave  = stub.crearLlave();
        byte[] serverPubKeyEnc = stub.obtenLlave(idLlave);
        
        LlaveCliente llaveCliente = new LlaveCliente(serverPubKeyEnc);
        byte[] clientPubKeyEnc = llaveCliente.obtenLlave();
        
        stub.coordLlave(idLlave, clientPubKeyEnc);
        llaveCliente.coordinaConServidor();
        System.out.println("\n");
        System.out.println("Bienvenido al sistema de números\n"
                    + "Este sistema te proporciona sumas y multiplicaciones distribuidas y coordinadas\n"
                    + "Opcion 1"
                    );
                
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); 
        int opcion = Integer.parseInt(reader.readLine());
            
        ArrayList arregloRes;
        byte[] pars;
        byte[] recovered;
        byte[] vuelo;
        byte[] parametroAMandar;
        
        try{
            switch(opcion){

                case 1: //si necesita mandar parámetros

                    System.out.println("Escribe el número que le quieres sumar a X");
                    opcion = Integer.parseInt(reader.readLine());
                    parametroAMandar = llaveCliente.encriptaMensaje(ByteBuffer.allocate(4).putInt(opcion).array());

                    vuelo = stub.sumaX(parametroAMandar, idLlave, clientPubKeyEnc,llaveCliente.obtenParametrosDeCifrado());
                    if(vuelo != null){
                        pars  = stub.obtenParametrosDeCifrado(idLlave, clientPubKeyEnc);
                        recovered = llaveCliente.decriptaMensaje(vuelo, pars);
                        System.out.println("El servidor no regresó respuesta");
                        arregloRes = (ArrayList) deserialize(recovered);
                        System.out.println(arregloRes.toString());
                    }
                    

                    break;

                default:
                    System.out.println("Opción no encontrada");
                    break;
            }
        }catch(Exception e){
            System.out.println("Error opciones lciente");
            System.out.println(e.toString());
            e.getStackTrace();
        }

            System.out.println("BYE =)");
    }
    
    public static byte[] serizlize(Object object){
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(baos != null){
                    baos.close();
                }
                if (oos != null) {
                    oos.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return null;
    }
    
    public static Object deserialize(byte[] bytes){
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try{
            bais = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bais);
            return ois.readObject();
        }catch(Exception e){
            System.out.println(e.toString());
            e.printStackTrace();
        }finally {
            try {

            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return null;
    }
}