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

/**
 *
 * @author mcc14
 */
public class Cliente {
    // inicializar las entradas y salidas
    private Socket socket = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;

    public Cliente(String ip, int puerto) {
        try{
            socket = new Socket(ip, puerto);
            System.out.println("Ya me conecté");
            
            // recibe entrada de la terminal
            in = new DataInputStream(System.in);
            
            // manda la salida al socket
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String mensaje = "";
        while(!mensaje.equals("Fin")){
            try{
                mensaje = in.readLine();
                out.writeUTF(mensaje);
            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }            
        }
        // cerrar conexion
        try{
            in.close();
            out.close();
            socket.close();
        }
        catch(IOException e){ 
            System.out.println(e); 
        } 
    }
    
    public static void main(String args[]){
        Cliente cliente = new Cliente("148.205.36.214", 5000);
    }
    
    
}
