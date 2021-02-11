package rmi;

import bd.Lugar;
import bd.Persona;
import bd.Vuelo;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class Client {

    private Client() {}

    public static void main(String[] args) {

        String host = (args.length < 1) ? "148.205.36.206" : args[0];
        try {
            System.setProperty("java.rmi.server.hostname", host);
            //Registry registry = LocateRegistry.getRegistry(1010);
            Registry registry = LocateRegistry.getRegistry(host, 1010);
            Hello stub = (Hello) registry.lookup("Hello");
            System.out.println("Aqui escribe el metodo que quieres probar");
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); 
            int opcion0, opcion = Integer.parseInt(reader.readLine());
            String cadenaOpcion = "";
            
            try{
                switch(opcion){
                    case 1:
                        ArrayList<Lugar> lugares = stub.obtenerLugares();
                        System.out.println(lugares.toString());
                        break;
                    case 2:
                        System.out.println("Escribe el número de vuelo");
                        opcion = Integer.parseInt(reader.readLine()); 
                        ArrayList<Persona> personas = stub.obtenerPersonasVuelo(opcion);
                        break;
                    case 3:
                        System.out.println("Escribe el número de vuelo");
                        opcion = Integer.parseInt(reader.readLine()); 
                        Vuelo vuelo = stub.obtenerVuelo(opcion);
                        System.out.println(vuelo.toString());
                        break;
                    case 4:
                        System.out.println("Escribe el id de la persona");
                        opcion = Integer.parseInt(reader.readLine()); 
                        System.out.println("Escribe la fecha");
                        cadenaOpcion = reader.readLine(); 
                        ArrayList<Vuelo> vuelosAntPersona = stub.vuelosAnterioresPersona(cadenaOpcion, opcion);
                        System.out.println(vuelosAntPersona.toString());
                        break;
                    case 5:
                        System.out.println("Escribe la fecha (aaaa-mm-dd)");
                        cadenaOpcion = reader.readLine();
                        ArrayList<Vuelo> vuelosDisp = stub.vuelosDisponibles(cadenaOpcion);
                        System.out.println(vuelosDisp.toString());
                        break;
                    case 6:
                        System.out.println("Escribe el id de la persona");
                        opcion = Integer.parseInt(reader.readLine()); 
                        System.out.println("Escribe la fecha");
                        cadenaOpcion = reader.readLine(); 
                        ArrayList<Vuelo> vuelosPersona = stub.vuelosDisponiblesPersona(cadenaOpcion, opcion);
                        System.out.println(vuelosPersona.toString());
                        break;
                    case 7:
                        ArrayList<Vuelo> vuelosHist = stub.vuelosHistoricos();
                        System.out.println(vuelosHist.toString());
                        break;
                    case 8:
                        System.out.println("Escribe el id de la persona");
                        opcion = Integer.parseInt(reader.readLine()); 
                        ArrayList<Vuelo> vuelosHistPersona = stub.vuelosHistoricosPersona(opcion);
                        System.out.println(vuelosHistPersona.toString());
                        break;
                    case 9:
                        System.out.println("Escribe el id del origen");
                        opcion = Integer.parseInt(reader.readLine()); 
                        System.out.println("Escribe el id del destino");
                        opcion0 = Integer.parseInt(reader.readLine()); 
                        ArrayList<Vuelo> vuelosOrigenDestino =stub.vuelosOrigenDestino(opcion, opcion0);
                        System.out.println(vuelosOrigenDestino.toString());
                        break;
                    default:
                        System.out.println("Opción no encontrada");
                        break;
                }
            }catch(Exception e){
                System.out.println("Error");
                System.out.println(e.toString());
            }
            
            
            
            
            System.out.println("BYE =)");
            
            
            
            
            
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}