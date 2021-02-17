package rmi;

import bd.Lugar;
import bd.Persona;
import bd.Vuelo;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import seguridad.LlaveCliente;

public class ClienteUnServer {

    private ClienteUnServer() {}

    public static void main(String[] args) {

        //String host = (args.length < 1) ? "148.205.36.206" : args[0];
        
        String host = (args.length < 1) ? "localhost" : args[0];
        
        try {
            System.setProperty("java.rmi.server.hostname", host);
            //Registry registry = LocateRegistry.getRegistry(1010);
            Registry registry = LocateRegistry.getRegistry(host, 1010);
            Hello stub = (Hello) registry.lookup("Hello");
            
            byte[] prueba  = stub.crearLlave(0);
            
            LlaveCliente llaveCliente = new LlaveCliente(prueba);
            // Bob encodes his public key, and sends it over to Alice.
            byte[] bobPubKeyEnc = llaveCliente.obtenLlave();
            
            stub.coordLlave(bobPubKeyEnc);
            llaveCliente.coordinaConServidor();
             
            byte[] prueba2  = stub.enviarPrueba();
            byte[] pars  = stub.obtenParametrosDeCifrado();
            
            System.out.println(prueba2.toString());
            System.out.println(pars.toString());
            
            byte[] prueba3 = llaveCliente.decriptaMensaje(prueba2, pars);
            
            System.out.println(prueba3.toString());
            
            ByteArrayInputStream in = new ByteArrayInputStream(prueba3);
            ObjectInputStream is = new ObjectInputStream(in);
            Object res = is.readObject();
            Persona resPersona = (Persona) res;        
            System.out.println(resPersona.toString());
            
            
            
         } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
            
            
        
           
        
        
        
//        
//        try {
//            System.setProperty("java.rmi.server.hostname", host);
//            //Registry registry = LocateRegistry.getRegistry(1010);
//            Registry registry = LocateRegistry.getRegistry(host, 1010);
//            Hello stub = (Hello) registry.lookup("Hello");
//            System.out.println("Bienvenido al sistema de vuelos de la aerolínea 'Distributed friends'. \n"
//                    + "Este sistema te proporciona datos útiles para localizar vuelos, lugares y personas \n"
//                    + "registradas en el sistema. El menú principal contiene 9 métodos identificados \n"
//                    + "con números: \n" 
//                    + "\n" 
//                    + "1 - Obtener todos los destinos a los cuales viaja la aerolinea \n"
//                    + "    No recibe como entrada ningún parámetro  \n"
//                    + "2 - Obtener todos los pasajeros registrados en un vuelo \n"
//                    + "    Recibe como entrada el id del vuelo a buscar \n"
//                    + "3 - Obtener todos los datos registrados de un vuelo \n"
//                    + "    Recibe como entrada el id del vuelo a buscar \n"
//                    + "4 - Obtener todos los vuelos registrados de un pasajero anteriores a una fecha \n"
//                    + "    Recibe como entrada el id del pasajero y la fecha máxima de viaje\n"
//                    + "5 - Obtener los vuelos disponibles a partir de una fecha\n"
//                    + "    Recibe como entrada la fecha a partir de la cual buscar \n"
//                    + "6 - Obtener los vuelos disponibles a partir de una fecha para un pasajero\n"
//                    + "    Recibe como entrada el id del pasajero y la fecha a partir de la cual buscar \n"
//                    + "7 - Obtener todos los vuelos históricos en la aerolínea\n"
//                    + "    No recibe como entrada ningún parámetro \n"
//                    + "8 - Obtener todos los vuelos históricos para una persona\n"
//                    + "    Recibe como entrada el id del pasajero  \n"
//                    + "9 - Obtener todos los vuelos con un origen y un destino específico\n"
//                    + "    Recibe como entrada el id del lugar de origen y el id del lugar destino\n"
//                    + "\n" 
//                    + "Escribe el número del método a ejecutar seguido de los parámetros indicados:" 
//                    + "");
//            
//            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); 
//            int opcion0, opcion = Integer.parseInt(reader.readLine());
//            String cadenaOpcion = "";
//            
//            
//            try{
//                switch(opcion){
//                    case 1:
//                        ArrayList<Lugar> lugares = stub.obtenerLugares();
//                        System.out.println(lugares.toString());
//                        break;
//                    case 2:
//                        System.out.println("Escribe el número de vuelo");
//                        opcion = Integer.parseInt(reader.readLine()); 
//                        ArrayList<Persona> personas = stub.obtenerPersonasVuelo(opcion);
//                        System.out.println(personas.toString());
//                        break;
//                    case 3:
//                        System.out.println("Escribe el número de vuelo");
//                        opcion = Integer.parseInt(reader.readLine()); 
//                        Vuelo vuelo = stub.obtenerVuelo(opcion);
//                        System.out.println(vuelo.toString());
//                        break;
//                    case 4:
//                        System.out.println("Escribe el id de la persona");
//                        opcion = Integer.parseInt(reader.readLine()); 
//                        System.out.println("Escribe la fecha");
//                        cadenaOpcion = reader.readLine(); 
//                        ArrayList<Vuelo> vuelosAntPersona = stub.vuelosAnterioresPersona(cadenaOpcion, opcion);
//                        System.out.println(vuelosAntPersona.toString());
//                        break;
//                    case 5:
//                        System.out.println("Escribe la fecha (aaaa-mm-dd)");
//                        cadenaOpcion = reader.readLine();
//                        ArrayList<Vuelo> vuelosDisp = stub.vuelosDisponibles(cadenaOpcion);
//                        System.out.println(vuelosDisp.toString());
//                        break;
//                    case 6:
//                        System.out.println("Escribe el id de la persona");
//                        opcion = Integer.parseInt(reader.readLine()); 
//                        System.out.println("Escribe la fecha");
//                        cadenaOpcion = reader.readLine(); 
//                        ArrayList<Vuelo> vuelosPersona = stub.vuelosDisponiblesPersona(cadenaOpcion, opcion);
//                        System.out.println(vuelosPersona.toString());
//                        break;
//                    case 7:
//                        ArrayList<Vuelo> vuelosHist = stub.vuelosHistoricos();
//                        System.out.println(vuelosHist.toString());
//                        break;
//                    case 8:
//                        System.out.println("Escribe el id de la persona");
//                        opcion = Integer.parseInt(reader.readLine()); 
//                        ArrayList<Vuelo> vuelosHistPersona = stub.vuelosHistoricosPersona(opcion);
//                        System.out.println(vuelosHistPersona.toString());
//                        break;
//                    case 9:
//                        System.out.println("Escribe el id del origen");
//                        opcion = Integer.parseInt(reader.readLine()); 
//                        System.out.println("Escribe el id del destino");
//                        opcion0 = Integer.parseInt(reader.readLine()); 
//                        ArrayList<Vuelo> vuelosOrigenDestino =stub.vuelosOrigenDestino(opcion, opcion0);
//                        System.out.println(vuelosOrigenDestino.toString());
//                        break;
//                    default:
//                        System.out.println("Opción no encontrada");
//                        break;
//                }
//            }catch(Exception e){
//                System.out.println("Error");
//                System.out.println(e.toString());
//            }
//            
//            
//            
//            
//            System.out.println("BYE =)");
//            
//            
//            
//            
//            
//        } catch (Exception e) {
//            System.err.println("Client exception: " + e.toString());
//            e.printStackTrace();
//        }
    }
}