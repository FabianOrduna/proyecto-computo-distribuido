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
        
    }
    
    public ManejadorSockets(Nodo [] n) throws IOException{
        this.nodos = new ArrayList();
        this.connSockets = new ArrayList();
        this.outputs = new ArrayList();
        //this.nodos.add(new Nodo("148.205.36.206",5000, 214));
        
        for (int i = 0; i < n.length; i++) {
            this.nodos.add(n[i]);
        }        
    }
    
    public void mandaInstrucciones(Mensaje inst) throws IOException{
        
        if(this.connSockets.isEmpty()){
            Socket s;
            
            for (int i = 0; i < nodos.size(); i++) {
                if(this.nodos.get(i).isMantenerAbierto()){
                    s= new Socket(this.nodos.get(i).getHost(), this.nodos.get(i).getPort());
                    this.connSockets.add(s);
                    System.out.println("Añadiendo el output"+this.nodos.get(i).getHost());
                    this.outputs.add(new DataOutputStream(s.getOutputStream()));
                    System.out.println("Después de añadir el output");
                }
                
            }
        }
        
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
    
    public void mandaReleaseATodos(String json) throws IOException{
        
        Nodo n;
        for (int i = 0; i < nodos.size(); i++) {
            n = nodos.get(i);
            try{
                
                mandaReleaseANodo(json, n);
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
    
    public void mandaJSONANodoPorIdentificador(int identificador, String json) throws IOException{
        
        if(this.connSockets.isEmpty()){
            Socket s;
            
            for (int i = 0; i < nodos.size(); i++) {
                if(this.nodos.get(i).isMantenerAbierto()){
                    s= new Socket(this.nodos.get(i).getHost(), this.nodos.get(i).getPort());
                    this.connSockets.add(s);
                    System.out.println("Añadiendo el output"+this.nodos.get(i).getHost());
                    this.outputs.add(new DataOutputStream(s.getOutputStream()));
                    System.out.println("Después de añadir el output");
                }
                
            }
        }
        
        Socket s = null;
        DataOutputStream out;
        int i  = 0;
        System.out.println("El total de nodos son "+this.nodos.size());
        System.out.println("El total de sockets son "+this.connSockets.size());
        System.out.println("El total de outputs son "+this.outputs.size());
        while(this.nodos.get(i).getId() != identificador){
            i++;
        }
        if(this.nodos.get(i).isMantenerAbierto()){
            out = this.outputs.get(i);
        }else{
            System.out.println("El host "+this.nodos.get(i).getHost()+" se maneja abriendo y cerrando conexion.");
            System.out.println("Abriendo conexion con "+this.nodos.get(i).getHost());
            s= new Socket(this.nodos.get(i).getHost(), this.nodos.get(i).getPort());
            out = new DataOutputStream(s.getOutputStream());
        }
        //out.writeUTF(json);
        out.write(json.getBytes());
        System.out.println("Reply enviado a "+identificador);
        
        if(!this.nodos.get(i).isMantenerAbierto()){
            out.close();
            s.close();
            System.out.println("Conexion cerrada con "+this.nodos.get(i).getHost());
        }
 
    }    
    public void mandaInstruccionANodo(Mensaje inst, Nodo nod){
        Socket socket;
        DataOutputStream out;
        String mensaje;
        String target;
        String action;
        try {
            System.out.println("Recuperando socket "+nod.getHost());
            if(nod.isMantenerAbierto()){
                socket = recuperaSocketPorHost(nod.getHost());
                out = recuperaOutputPorHost(nod.getHost()); 
            }else{
                socket= new Socket(nod.getHost(), nod.getPort());
                out = new DataOutputStream(socket.getOutputStream());
            }            
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
            
            mensaje = "{\"action\":\""+action+"\", \"value\":"+inst.getValor()+", \"target\":\""+target+"\", \"sender\":\""+inst.getIdentificador()+"\", \"time\":\""+inst.getTiempo()+"\"}\n";
            //mensaje = "Hola";
            System.out.println("Enviando mensaje a :"+nod.getHost());
            System.out.println("Mensaje a enviar: "+mensaje);
            //out.writeUTF(mensaje);
            out.write(mensaje.getBytes());
            System.out.println("Después de enviar");
            //in.close();
            
            if(!nod.isMantenerAbierto()){
                out.close();
                socket.close();
            }
                        
        } catch (Exception ex) {
            Logger.getLogger(ManejadorSockets.class.getName()).log(Level.SEVERE, null, ex);
            //System.out.println(ex.toString());
        }
        
    }
      
    public void mandaReleaseANodo(String mensajeJson, Nodo nod){
        Socket socket;
        DataOutputStream out;
        String mensaje;
        String target;
        String action;
        try {
            System.out.println("Recuperando socket "+nod.getHost());
            if(nod.isMantenerAbierto()){
                socket = recuperaSocketPorHost(nod.getHost());
                out = recuperaOutputPorHost(nod.getHost()); 
            }else{
                socket= new Socket(nod.getHost(), nod.getPort());
                out = new DataOutputStream(socket.getOutputStream());
            } 
            
            //in = new DataInputStream(System.in);
   
            System.out.println("Enviando mensaje a :"+nod.getHost());
            System.out.println("Mensaje a enviar: "+mensajeJson);
            //out.writeUTF(mensajeJson);
            out.write(mensajeJson.getBytes());
            
            System.out.println("Después de enviar");
            //in.close();
            
            if(!nod.isMantenerAbierto()){
                out.close();
                socket.close();
            }
            
        } catch (Exception ex) {
            Logger.getLogger(ManejadorSockets.class.getName()).log(Level.SEVERE, null, ex);
            //System.out.println(ex.toString());
        }
        
    }
    
    
}
