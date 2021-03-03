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

/**
 *
 * @author mcc06
 */
public class ManejadorSockets {
    private ArrayList<Nodo> nodos;
    
    public ManejadorSockets(){
        this.nodos = new ArrayList();
        //this.nodos.add(new Nodo("148.205.36.206",5000, 214));
        
        this.nodos.add(new Nodo("148.205.36.218",5000, 218));
        this.nodos.add(new Nodo("148.205.36.214",5000, 214));
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
            Logger.getLogger(ManejadorSockets.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.toString());
        }
        
    }
    
    public static void main(String[] args) throws IOException {
        ManejadorSockets ms = new ManejadorSockets();
        ClaseInstrucciones ci = new ClaseInstrucciones(876543210, 2, "ADDX", 3);
        ms.mandaInstrucciones(ci);
    }
    
}
