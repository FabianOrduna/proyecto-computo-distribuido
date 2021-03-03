/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sockets;

import java.net.*;
import java.io.*;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 *
 * @author mcc06
 */
public class ManejadorSockets extends Thread {
    private ArrayList<Nodo> nodos;
    private final int puertoEscuchador = 5000;
    private ColaDePrioridad log;
    private Socket socket = null;
    private ServerSocket servidor = null;
    private DataInputStream in = null;
    
    public ManejadorSockets(){
        this.nodos = new ArrayList();
        //this.nodos.add(new Nodo("148.205.36.206",5000, 214));
        
        this.nodos.add(new Nodo("148.205.36.218",5000, 218));
        this.nodos.add(new Nodo("148.205.36.214",5000, 214));
        this.log = new ColaDePrioridad();
    }
    
    public void mandaInstrucciones(ClaseInstrucciones inst) throws IOException{
        Nodo n;
        for (int i = 0; i < nodos.size(); i++) {
            n = nodos.get(i);
            mandaInstruccionANodo(inst, n);
        }
    }
    
    public void mandaInstruccionANodo(ClaseInstrucciones inst, Nodo nod){
        Socket socket = null;
        DataOutputStream out = null;
        DataInputStream in = null;
        String mensaje;
        String target;
        String action;
        try {
            socket = new Socket(nod.getHost(), nod.getPort());
            out = new DataOutputStream(socket.getOutputStream());
            //in = new DataInputStream(System.in);
            
            if(inst.getInstruccion() == inst.ADD_X || inst.getInstruccion() == inst.MULTIPLY_X){
                target = "x";
            }else{
                target = "y";
            }
            
            if(inst.getInstruccion() == inst.ADD_X || inst.getInstruccion() == inst.ADD_Y){
                action = "ADD";
            }else{
                action = "MULTIPLY";
            }
            
            mensaje = "{\"action\":\""+action+"\", \"value\":"+inst.getValor()+", \"target\":\""+target+"\", \"sender\":\""+inst.getIdentificador()+"\", \"time\":\""+inst.getTiempo()+"\"}";
            //mensaje = "Hola";
            System.out.println("Enviando mensaje a :"+nod.getHost());
            System.out.println("Mensaje a enviar: "+mensaje);
            out.writeUTF(mensaje);
            System.out.println("Después de enviar");
            //in.close();
            out.close();
            socket.close();
        } catch (Exception ex) {
            //Logger.getLogger(ManejadorSockets.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.toString());
        }
        
    }
    
    @Override
    public void run()  
    {   
        
        
        // el servidor espera a que llegue una conexion
        try{
            this.servidor = new ServerSocket(this.puertoEscuchador);
            System.out.println("El servidor está esperando un cliente");
            
         
            
            
            // recibir entrada del socket del cliente
            
            String mensaje = "";
            
            // receive JSON message
            JSONObject jsonObject;
            
            
            ClaseInstrucciones ci;
            
            // leer mensaje hasta recibir Fin
            //int num = 0;
            while(true){
                this.socket = servidor.accept();
                System.out.println("Cliente aceptado");
                this.in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                try{
                    //
                    if(this.in.available() >0){
                        mensaje = this.in.readUTF();
                    
                    
                        // convert JSON message - { "action": "ADD", "value": 1, "target": "x", "sender":"14", "time":"5"}

                        if(mensaje!=null){


                        jsonObject = new JSONObject(mensaje);
                        System.out.println("action"+jsonObject.get("action"));
                        System.out.println("value"+jsonObject.get("value"));
                        System.out.println("target"+jsonObject.get("target"));
                        System.out.println("sender"+jsonObject.get("sender"));
                        System.out.println("time"+jsonObject.get("time"));
                        ci = new ClaseInstrucciones(Integer.parseInt(jsonObject.get("sender").toString()), 
                                           Integer.parseInt(jsonObject.get("time").toString()),
                                           jsonObject.get("action").toString()+jsonObject.get("target").toString(),
                                           Integer.parseInt(jsonObject.get("value").toString()));
                        log.agregaInstruccion(ci);
                        System.out.println(log.toString());

                        mensaje = null;
                        }
                    }else{
                        //System.out.println("n");
                    }
                } catch (Exception ex) {
                    Logger.getLogger(Escuchador.class.getName()).log(Level.SEVERE, null, ex);
                    //socket.close();
                    //in.close();
                }
                //num++;
                //num++;
            }
            
            //System.out.println("Cerrando conexión");
            //socket.close();
            //in.close();
            
        }
        catch(Exception e){ 
            System.out.println(e); 
        }
    }
    
    public static void main(String[] args) throws IOException {
        ManejadorSockets ms = new ManejadorSockets();
        ms.start();
        //ClaseInstrucciones ci = new ClaseInstrucciones(876543210, 2, "ADDX", 3);
        //ms.mandaInstrucciones(ci);
    }
    
}
