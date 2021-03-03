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
public class ManejadorSockets extends Thread {
    private ArrayList<Nodo> nodos;
    private ArrayList<Socket> connSockets;
    private ArrayList<DataOutputStream> outputs;
    
    public ManejadorSockets() throws IOException{
        this.nodos = new ArrayList();
        this.connSockets = new ArrayList();
        this.outputs = new ArrayList();
        //this.nodos.add(new Nodo("148.205.36.206",5000, 214));
        
        this.nodos.add(new Nodo("148.205.36.218",5056, 218));
        this.nodos.add(new Nodo("148.205.36.214",5056, 214));
        
        Socket s;
        for (int i = 0; i < nodos.size(); i++) {
            s= new Socket(this.nodos.get(i).getHost(), this.nodos.get(i).getPort());
            this.connSockets.add(s);
            this.outputs.add(new DataOutputStream(s.getOutputStream()));
        }
    }
    
    public void mandaInstrucciones(ClaseInstrucciones inst){
        Nodo n;
        for (int i = 0; i < nodos.size(); i++) {
            n = nodos.get(i);
            try{
                
                mandaInstruccionANodo(inst, n);
            }catch(Exception e){
                System.out.println(e.toString());
            }
            
        }
    }
    
    public Socket recuperaSocketPorHost(String host){
        int i  = 0;
        while(!this.nodos.get(i).getHost().equals(host)){
            i++;
        }
        return this.connSockets.get(i);
    }
    
    public DataOutputStream recuperaOutputPorHost(String host){
        int i  = 0;
        while(!this.nodos.get(i).getHost().equals(host)){
            i++;
        }
        return this.outputs.get(i);
    }
    
    public void mandaInstruccionANodo(ClaseInstrucciones inst, Nodo nod){
        Socket socket;
        DataOutputStream out;
        String mensaje;
        String target;
        String action;
        try {
            System.out.println("Recuperando socket "+nod.getHost());
            socket = recuperaSocketPorHost(nod.getHost());
            out = recuperaOutputPorHost(nod.getHost());
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
            
            //out.close();
            //socket.close();
            
        } catch (Exception ex) {
            Logger.getLogger(ManejadorSockets.class.getName()).log(Level.SEVERE, null, ex);
            //System.out.println(ex.toString());
        }
        
    }
    
}
