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
  
// Server class 
public class ArribaConRMI{
    
    static int numero = 1;
    
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
        while (true)  
        { 
            try { 
  
                // Ask user what he wants 
                dos.writeUTF("What do you want?[Sumar | Multiplicar]..\n"+ 
                            "Type Exit to terminate connection."); 
                  
                // receive the answer from client 
                received = dis.readUTF(); 
                System.out.println("Lo que quiere el cliente"+s.toString()+" es: "+received);
                System.out.println("Valor actual de la variable: "+ArribaConRMI.numero);
                
                if(received.equals("Exit")) 
                {  
                    System.out.println("Client " + this.s + " sends exit..."); 
                    System.out.println("Closing this connection."); 
                    this.s.close(); 
                    System.out.println("Connection closed"); 
                    break; 
                } 
                  
                // write on output stream based on the 
                // answer from the client 
                switch (received) { 
                  
                    case "Sumar" : 
                        ArribaConRMI.numero++;
                        System.out.println("quiere sumar"); 
                        dos.writeUTF(""+ArribaConRMI.numero); 
                        break; 
                          
                    case "Multiplicar" : 
                         ArribaConRMI.numero*=2;
                        System.out.println("quiere multiplicar"); 
                        dos.writeUTF(""+ArribaConRMI.numero); 
                        break; 
                          
                    default: 
                        dos.writeUTF("Invalid input"); 
                        break; 
                } 
            } catch (IOException e) { 
                e.printStackTrace(); 
            }
            
            System.out.println("Valor de variable después de operación: "+ArribaConRMI.numero);
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