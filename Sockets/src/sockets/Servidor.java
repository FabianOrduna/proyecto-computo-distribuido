/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sockets;

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author mcc14
 */
public class Servidor {
    
    // inicialiar sockt y entrada
    private Socket socket = null;
    private ServerSocket servidor = null;
    private DataInputStream in = null;
    
    public Servidor(int puerto){
        
        // el servidor espera a que llegue una conexion
        try{
            servidor = new ServerSocket(puerto);
            System.out.println("El servidor está esperando un cliente");
            
            socket = servidor.accept();
            System.out.println("Cliente aceptado");
            
            // recibir entrada del socket del cliente
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            String mensaje = "";
            
            // receive JSON message
            JSONObject jsonObject;
            
            ColaDePrioridad log = new ColaDePrioridad();
            ClaseInstrucciones ci;
            
            // leer mensaje hasta recibir Fin
            //int num = 0;
            while(true){
                try{
                    //
                    if(in.available() >0){
                        mensaje = in.readUTF();
                    
                    
                        // convert JSON message - { "action": "ADD", "value": 1, "target": "x", "sender":"14", "time":"5"}

                        if(mensaje!=null){


                        jsonObject = new JSONObject(mensaje);
                        System.out.println(jsonObject.get("action"));
                        System.out.println(jsonObject.get("value"));
                        System.out.println(jsonObject.get("target"));
                        System.out.println(jsonObject.get("sender"));
                        System.out.println(jsonObject.get("time"));
                        ci = new ClaseInstrucciones(Integer.parseInt(jsonObject.get("sender").toString()), 
                                           Integer.parseInt(jsonObject.get("time").toString()),
                                           jsonObject.get("action").toString()+jsonObject.get("target").toString(),
                                           Integer.parseInt(jsonObject.get("value").toString()));
                        log.agregaInstruccion(ci);
                        System.out.println(log.toString());

                        mensaje = null;
                        }
                    }
                } catch (Exception ex) {
                    Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                    socket.close();
                    in.close();
                }
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
    
    public static void main(String args[]){
        Servidor s = new Servidor(5000);
    }
    
}
