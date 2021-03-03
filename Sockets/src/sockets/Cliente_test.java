package sockets;
// Java implementation for a client 
// Save file as Client.java 
 
import java.io.*; 
import java.net.*; 
import java.util.Scanner; 
import java.util.logging.Level;
import java.util.logging.Logger;
import sockets.Cliente;
  
// Client class 
public class Cliente_test  
{ 
    private static Socket s = null;
    private static DataOutputStream out = null;
    
    public static void main(String[] args) throws IOException  
    { 
        try
        { 
            //in = new DataInputStream(System.in);
            // getting localhost ip 
            InetAddress ip = InetAddress.getByName("148.205.36.206"); 
      
            // establish the connection with server port 5056 
            s = new Socket(ip, 5056);
            out = new DataOutputStream(s.getOutputStream()); 

        }catch(IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
      
            // obtaining input and out streams 
      
            // the following loop performs the exchange of 
            // information between client and client handler 
            int num = 0;
            while(num<1){   
                try{
                    //mensaje = in.readLine();
                    out.writeUTF("{ \"action\": \"MULTIPLY\", \"value\": 5, \"target\": \"y\", \"sender\":\""+num*1000+"\", \"time\":\"3\"}");
                } catch (Exception ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                }
                num ++;
            }
         try{
            //in.close();
            out.close();
            s.close();
        }
        catch(Exception e){ 
            System.out.println(e); 
        } 
    } 
} 