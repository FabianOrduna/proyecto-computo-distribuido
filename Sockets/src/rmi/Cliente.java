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
import java.sql.SQLException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import seguridad.LlaveCliente;

public class Cliente {

    private Cliente() {}

    public static void main(String[] args) throws RemoteException, NotBoundException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException, InvalidKeyException, NoSuchPaddingException, IOException {

        String host = (args.length < 1) ? "148.205.36.206" : args[0];
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
                    + "\tOpcion 1: Sumarle a X\n"
                    + "\tOpcion 2: Sumarle a Y\n"
                    + "\tOpcion 3: Obtener X\n"
                    + "\tOpcion 4: Obtener Y\n"
                    + "\tOpcion 5: Multiplicar X\n"
                    + "\tOpcion 6: Multiplicar Y\n"
                    );
                
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); 
        int opcion = Integer.parseInt(reader.readLine());
            
        byte[] recovered;
        byte[] pars;
        byte[] res;
        int val;

        byte[] parametroAMandar;
        
        try{
            switch(opcion){

                case 1: //si necesita mandar parámetros
                    System.out.println("Escribe el número que le quieres sumar a X");
                    opcion = Integer.parseInt(reader.readLine());
                    parametroAMandar = llaveCliente.encriptaMensaje(ByteBuffer.allocate(4).putInt(opcion).array());
                    stub.sumaX(parametroAMandar, idLlave, clientPubKeyEnc,llaveCliente.obtenParametrosDeCifrado());
                    break;
                
                case 2: //si necesita mandar parámetros
                    System.out.println("Escribe el número que le quieres sumar a Y");
                    opcion = Integer.parseInt(reader.readLine());
                    parametroAMandar = llaveCliente.encriptaMensaje(ByteBuffer.allocate(4).putInt(opcion).array());
                    stub.sumaY(parametroAMandar, idLlave, clientPubKeyEnc,llaveCliente.obtenParametrosDeCifrado());
                    break;
                    
                case 3: //si necesita mandar parámetros
                    res = stub.getX(llaveCliente.obtenParametrosDeCifrado());
                    pars = stub.obtenParametrosDeCifrado(idLlave, clientPubKeyEnc);
                    recovered = llaveCliente.decriptaMensaje(res, pars);
                    val = ByteBuffer.wrap(recovered).getInt();
                    System.out.println("El valor de X es: "+val);
                    break;
                    
                case 4: //si necesita mandar parámetros
                    res = stub.getY(llaveCliente.obtenParametrosDeCifrado());
                    pars = stub.obtenParametrosDeCifrado(idLlave, clientPubKeyEnc);
                    recovered = llaveCliente.decriptaMensaje(res, pars);
                    val = ByteBuffer.wrap(recovered).getInt();
                    System.out.println("El valor de Y es: "+val);
                    break;
                    
                case 5: //si necesita mandar parámetros
                    System.out.println("Escribe el número que le quieres multiplicar a X");
                    opcion = Integer.parseInt(reader.readLine());
                    parametroAMandar = llaveCliente.encriptaMensaje(ByteBuffer.allocate(4).putInt(opcion).array());
                    stub.multiplicaX(parametroAMandar, idLlave, clientPubKeyEnc,llaveCliente.obtenParametrosDeCifrado());
                    break;
                
                case 6: //si necesita mandar parámetros
                    System.out.println("Escribe el número que le quieres multiplicar a Y");
                    opcion = Integer.parseInt(reader.readLine());
                    parametroAMandar = llaveCliente.encriptaMensaje(ByteBuffer.allocate(4).putInt(opcion).array());
                    stub.multiplicaY(parametroAMandar, idLlave, clientPubKeyEnc,llaveCliente.obtenParametrosDeCifrado());
                    break;

                default:
                    System.out.println("Opción no encontrada");
                    break;
            }
        }catch(IOException | NumberFormatException | InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | SQLException | BadPaddingException | IllegalBlockSizeException e){
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
        } catch (IOException e) {
        }finally {
            try {
                if(baos != null){
                    baos.close();
                }
                if (oos != null) {
                    oos.close();
                }
            } catch (IOException e2) {
            }
        }
        return null;
    }
    
    public static Object deserialize(byte[] bytes){
        ByteArrayInputStream bais;
        ObjectInputStream ois;
        try{
            bais = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bais);
            return ois.readObject();
        }catch(IOException | ClassNotFoundException e){
            System.out.println(e.toString());
        }finally {
            try {

            } catch (Exception e2) {
            }
        }
        return null;
    }
}