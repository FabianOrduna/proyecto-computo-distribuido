package rmi;

import bd.Lugar;
import bd.Persona;
import bd.Vuelo;
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

        //String host = (args.length < 1) ? "148.205.36.206" : args[0];
        
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
        System.out.println("Bienvenido al sistema de vuelos de la aerolínea 'Distributed friends'. \n"
                    + "Este sistema te proporciona datos útiles para localizar vuelos, lugares y personas \n"
                    + "registradas en el sistema. El menú principal contiene 9 métodos identificados \n"
                    + "con números: \n" 
                    + "\n" 
                    + "1 - Obtener todos los destinos a los cuales viaja la aerolinea \n"
                    + "    No recibe como entrada ningún parámetro  \n"
                    + "2 - Obtener todos los pasajeros registrados en un vuelo \n"
                    + "    Recibe como entrada el id del vuelo a buscar \n"
                    + "3 - Obtener todos los datos registrados de un vuelo \n"
                    + "    Recibe como entrada el id del vuelo a buscar \n"
                    + "4 - Obtener todos los vuelos registrados de un pasajero anteriores a una fecha \n"
                    + "    Recibe como entrada el id del pasajero y la fecha máxima de viaje\n"
                    + "5 - Obtener los vuelos disponibles a partir de una fecha\n"
                    + "    Recibe como entrada la fecha a partir de la cual buscar \n"
                    + "6 - Obtener los vuelos disponibles a partir de una fecha para un pasajero\n"
                    + "    Recibe como entrada el id del pasajero y la fecha a partir de la cual buscar \n"
                    + "7 - Obtener todos los vuelos históricos en la aerolínea\n"
                    + "    No recibe como entrada ningún parámetro \n"
                    + "8 - Obtener todos los vuelos históricos para una persona\n"
                    + "    Recibe como entrada el id del pasajero  \n"
                    + "9 - Obtener todos los vuelos con un origen y un destino específico\n"
                    + "    Recibe como entrada el id del lugar de origen y el id del lugar destino\n"
                    + "\n" 
                    + "Escribe el número del método a ejecutar seguido de los parámetros indicados:" 
                    + "");
                
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); 
        int opcion0, opcion = Integer.parseInt(reader.readLine());
        String cadenaOpcion = "";
            
        ArrayList arregloRes;
        byte[] pars;
        byte[] recovered;
        byte[] lugares;
        byte[] personas;
        Vuelo vueloC;
        byte[] vuelo;
        byte[] parametroAMandar,parametroAMandarStr;
        
            try{
                switch(opcion){
                    case 1: //no necesita mandar parámetros
                        lugares = stub.obtenerLugares(idLlave, clientPubKeyEnc);
                        pars = stub.obtenParametrosDeCifrado(idLlave, clientPubKeyEnc);
                        recovered = llaveCliente.decriptaMensaje(lugares, pars);
                        arregloRes = (ArrayList) deserialize(recovered);       
                        System.out.println(arregloRes.toString());
                        break;
                    case 2: //si necesita mandar parámetros
                        System.out.println("Escribe el número de vuelo");
                        opcion = Integer.parseInt(reader.readLine());
                        parametroAMandar = llaveCliente.encriptaMensaje(ByteBuffer.allocate(4).putInt(opcion).array());
                        //personas = stub.obtenerPersonasVuelo(opcion, idLlave, clientPubKeyEnc);
                        personas = stub.obtenerPersonasVuelo(parametroAMandar, idLlave, clientPubKeyEnc, llaveCliente.obtenParametrosDeCifrado());

                        pars  = stub.obtenParametrosDeCifrado(idLlave, clientPubKeyEnc);
                        recovered = llaveCliente.decriptaMensaje(personas, pars);
                        arregloRes = (ArrayList) deserialize(recovered);       
                        System.out.println(arregloRes.toString());

                        break;
                    case 3: //si necesita mandar parámetros
                        
                        System.out.println("Escribe el número de vuelo");
                        opcion = Integer.parseInt(reader.readLine());
                        parametroAMandar = llaveCliente.encriptaMensaje(ByteBuffer.allocate(4).putInt(opcion).array());

                        vuelo = stub.obtenerPersonasVuelo(parametroAMandar, idLlave, clientPubKeyEnc,llaveCliente.obtenParametrosDeCifrado());
                        pars  = stub.obtenParametrosDeCifrado(idLlave, clientPubKeyEnc);
                        recovered = llaveCliente.decriptaMensaje(vuelo, pars);
                        arregloRes = (ArrayList) deserialize(recovered);
                        System.out.println(arregloRes.toString());
              
                        break;
                        
                    case 4: //si necesita mandar parámetros
                         
                         
                        
                        System.out.println("Escribe el número de vuelo");
                        opcion = Integer.parseInt(reader.readLine());
                        parametroAMandar = llaveCliente.encriptaMensaje(ByteBuffer.allocate(4).putInt(opcion).array());
                        
                        System.out.println("Escribe la fecha");
                        cadenaOpcion = reader.readLine();
                        parametroAMandarStr = llaveCliente.encriptaMensaje(cadenaOpcion.getBytes());
                        
                        
                        byte[] vuelosAntPersona = stub.vuelosAnterioresPersona(parametroAMandarStr, parametroAMandar, idLlave, clientPubKeyEnc,llaveCliente.obtenParametrosDeCifrado());
                        pars  = stub.obtenParametrosDeCifrado(idLlave, clientPubKeyEnc);
                        recovered = llaveCliente.decriptaMensaje(vuelosAntPersona, pars);
                        arregloRes = (ArrayList) deserialize(recovered);
                        System.out.println(arregloRes.toString());
  
                        break;
                    case 5: //si necesita mandar parámetros
                        System.out.println("Escribe la fecha (aaaa-mm-dd)");
                        cadenaOpcion = reader.readLine();
                        byte[] vuelosDisp = stub.vuelosDisponibles(cadenaOpcion, idLlave, clientPubKeyEnc);
                        pars  = stub.obtenParametrosDeCifrado(idLlave, clientPubKeyEnc);
                        recovered = llaveCliente.decriptaMensaje(vuelosDisp, pars);
                        arregloRes = (ArrayList) deserialize(recovered);
                        System.out.println(arregloRes.toString());
                        break;
                    case 6: //si necesita mandar parámetros
                        System.out.println("Escribe el id de la persona");
                        opcion = Integer.parseInt(reader.readLine()); 
                        System.out.println("Escribe la fecha");
                        cadenaOpcion = reader.readLine(); 
                        byte[] vuelosPersona = stub.vuelosDisponiblesPersona(cadenaOpcion, opcion, idLlave, clientPubKeyEnc);
                        pars  = stub.obtenParametrosDeCifrado(idLlave, clientPubKeyEnc);
                        recovered = llaveCliente.decriptaMensaje(vuelosPersona, pars);
                        arregloRes = (ArrayList) deserialize(recovered);
                        System.out.println(arregloRes.toString());
                        break;
                    case 7:
                        byte[] vuelosHist = stub.vuelosHistoricos(idLlave, clientPubKeyEnc);
                        pars  = stub.obtenParametrosDeCifrado(idLlave, clientPubKeyEnc);
                        recovered = llaveCliente.decriptaMensaje(vuelosHist, pars);
                        arregloRes = (ArrayList) deserialize(recovered);
                        System.out.println(arregloRes.toString());
                        break;
                    case 8: //si necesita mandar parámetros
                        System.out.println("Escribe el id de la persona");
                        opcion = Integer.parseInt(reader.readLine()); 
                        byte[] vuelosHistPersona = stub.vuelosHistoricosPersona(opcion, idLlave, clientPubKeyEnc);
                        pars  = stub.obtenParametrosDeCifrado(idLlave, clientPubKeyEnc);
                        recovered = llaveCliente.decriptaMensaje(vuelosHistPersona, pars);
                        arregloRes = (ArrayList) deserialize(recovered);
                        System.out.println(arregloRes.toString());
                        break;
                    case 9: //si necesita mandar parámetros
                        System.out.println("Escribe el id del origen");
                        opcion = Integer.parseInt(reader.readLine()); 
                        System.out.println("Escribe el id del destino");
                        opcion0 = Integer.parseInt(reader.readLine()); 
                        byte[] vuelosOrigenDestino =stub.vuelosOrigenDestino(opcion, opcion0, idLlave, clientPubKeyEnc);
                        pars  = stub.obtenParametrosDeCifrado(idLlave, clientPubKeyEnc);
                        recovered = llaveCliente.decriptaMensaje(vuelosOrigenDestino, pars);
                        arregloRes = (ArrayList) deserialize(recovered);
                        System.out.println(arregloRes.toString());    
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