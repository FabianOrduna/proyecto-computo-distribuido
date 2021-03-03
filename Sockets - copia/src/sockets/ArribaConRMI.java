package sockets;

// Java implementation of  Server side 
// It contains two classes : Server and ClientHandler 
// Save file as Server.java 
  
import java.io.*; 
import java.text.*; 
import java.util.*; 
import java.net.*; 
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
  
// Server class 
public class ArribaConRMI{
    
    static ColaDePrioridad log = new ColaDePrioridad();
    
    public static void main(String[] args) {


        SocketGeekForSeek manejador = new SocketGeekForSeek();
        manejador.start();
    }

public static class SocketGeekForSeek extends Thread
{     
    @Override
    public void run()  
    { 
        try {
            // server is listening on port 5056
            ServerSocket ss = new ServerSocket(5056);
            
            // running infinite loop for getting
            // client request
            while (true)
            {
                Socket s = null;
                
                try
                {
                    // socket object to receive incoming client requests
                    //System.out.println("Esperando a cliente");
                    s = ss.accept();
                    
                    System.out.println("A new client is connected : " + s);
                    
                    // obtaining input and out streams
                    DataInputStream dis = new DataInputStream(s.getInputStream());
                    DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                    
                    System.out.println("Assigning new thread for this client");
                    
                    // create a new thread object
                    Thread t = new ClientHandler(s, dis, dos);
                    
                    // Invoking the start() method
                    t.start();
                    
                }
                catch (Exception e){
                    s.close();
                    e.printStackTrace();
                } 
            }
        } catch (IOException ex) {
            //Logger.getLogger(SocketGeekForSeek.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
} 

// ClientHandler class 
static class ClientHandler extends Thread  
{ 
    
    final DataInputStream dis; 
    final DataOutputStream dos; 
    final Socket s; 
      
  
    // Constructor 
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos)
    { 
        this.s = s; 
        this.dis = dis; 
        this.dos = dos; 
    } 
  
    @Override
    public void run()  
    { 
        String received;
        JSONObject jsonObject;
        ClaseInstrucciones ci;
        while (true)  
        {   
            
            try { 
  
                // Ask user what he wants 
                /*dos.writeUTF("What do you want?[Sumar | Multiplicar]..\n"+ 
                            "Type Exit to terminate connection.");
*/
                  
                // receive the answer from client 
                received = dis.readUTF(); 
                //System.out.println("Lo que quiere el cliente"+s.toString()+" es: "+received);
                //System.out.println("Valor actual de la variable: "+ArribaConRMI.numero);
                
                if(received.equals("Exit")) 
                {  
                    System.out.println("Client " + this.s + " sends exit..."); 
                    //System.out.println("Closing this connection."); 
                    this.s.close(); 
                    //System.out.println("Connection closed"); 
                    break; 
                } 
                
                if(received.contains("release")){
                    System.out.println("Solicitud de release");
                }else{
                    if(received.contains("reply")){
                        System.out.println("Solicitud de reply realizada");
                    }else{
                        jsonObject = new JSONObject(received);
                        ci = new ClaseInstrucciones(Integer.parseInt(jsonObject.get("sender").toString()), 
                                           Integer.parseInt(jsonObject.get("time").toString()),
                                           jsonObject.get("action").toString()+jsonObject.get("target").toString(),
                                           Integer.parseInt(jsonObject.get("value").toString()));
                        log.agregaInstruccion(ci);
                        
                        System.out.println("Estatus actual de la cola de prioridad");
                        System.out.println(log.toString());
                    }
                }
                 
            } catch (IOException  e) { 
                //System.out.println(e.toString());
                
            } catch (JSONException ex) {
                //Logger.getLogger(ArribaConRMI.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        } 
          
        try
        { 
            // closing resources 
            this.dis.close(); 
            this.dos.close(); 
              
        }catch(IOException e){ 
            e.printStackTrace(); 
        } 
    } 
} 
}