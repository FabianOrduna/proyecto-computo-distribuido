package rmi;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.rmi.AlreadyBoundException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;     
import java.util.Hashtable;
import org.json.JSONException;
import org.json.JSONObject;
import seguridad.ManejadorLlaves;
import sockets.ClaseInstrucciones;
import sockets.ColaDePrioridad;
import sockets.ManejadorSockets;

public class Server extends UnicastRemoteObject implements Hello {
    
    private ManejadorLlaves manejadorLlaves;
    private static int reloj;
    private int x;
    private int y;
    public static ColaDePrioridad LOG = new ColaDePrioridad();
    private int identificador;
    private ManejadorSockets manejadorServidores;
    public static final int NUM_NODOS = 3; //esto incluye al nodo actual
    public static final int NUM_VECINOS = 2;
    public static Hashtable reply = new Hashtable();
    
    
    
    
    public Server(int identificador) throws RemoteException, SQLException, AlreadyBoundException, IOException{

        this.manejadorLlaves = new ManejadorLlaves();
        this.reloj = 1;
        this.x = 0;
        this.y = 0;
        this.identificador = identificador;
        this.manejadorServidores = new ManejadorSockets();
        
        reply.put(218,0 );
        reply.put(214,0 );
        //reply.put(206,0);
        
    }
    
    public byte[] serializa(Object objeto) { 
        try{
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(objeto);
        byte[] cleartext = out.toByteArray();
        return cleartext;
        }catch(Exception e){
            System.out.println(e.toString());
            return null;
        }
    }
    
    @Override
    public int crearLlave() throws RemoteException{
        //System.out.println("Creando llave");
        return manejadorLlaves.crearLlave();
    }
    
    @Override
    public byte[] obtenLlave(int llaveId) throws RemoteException{
        //System.out.println("Regresando llave");
        
        return manejadorLlaves.obtenerLlave(llaveId);
    }
    
    @Override
    public void coordLlave(int llaveId, byte[] clientPubKeyEnc) throws RemoteException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException{
        manejadorLlaves.coordLlave(llaveId,clientPubKeyEnc);
        //manejadorLlaves.imprimeSecreto(int llaveId);
    }   
        
    @Override
    public byte[] obtenParametrosDeCifrado(int llaveId, byte[] clientPubKeyEnc) throws IOException{
        return manejadorLlaves.obtenParametrosDeCifrado(llaveId);
    }    
    
    @Override
    public void sumaX(byte[] idVuelo, int llaveId, byte[] clientPubKeyEnc, byte[] paramsEncriptClient) throws RemoteException, SQLException, IOException {
        ClaseInstrucciones inst;
        System.out.println("Operacion de suma recibida en el servidor");
        int tmpIdVuelo = 0;
        System.out.println("Parametro de entrada: ");
        System.out.println(idVuelo);
        try {
            tmpIdVuelo = ByteBuffer.wrap(this.manejadorLlaves.desencripta(clientPubKeyEnc, idVuelo, paramsEncriptClient)).getInt();
            System.out.println("Número a sumar a X:"+tmpIdVuelo);
            System.out.println("Fin de desencripcion");
            this.reloj ++;
            inst = new ClaseInstrucciones(this.identificador,  this.reloj,  1,  tmpIdVuelo);
            LOG.agregaInstruccion(inst);
            
            this.manejadorServidores.mandaInstrucciones(inst);
            
            this.x+=tmpIdVuelo;
            
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }
    
    @Override
    public void sumaY(byte[] idVuelo, int llaveId, byte[] clientPubKeyEnc, byte[] paramsEncriptClient) throws RemoteException, SQLException, IOException {
        ClaseInstrucciones inst;
        System.out.println("Operacion de suma recibida en el servidor");
        int tmpIdVuelo = 0;
        System.out.println("Parametro de entrada: ");
        System.out.println(idVuelo);
        try {
            tmpIdVuelo = ByteBuffer.wrap(this.manejadorLlaves.desencripta(clientPubKeyEnc, idVuelo, paramsEncriptClient)).getInt();
            System.out.println("Número a sumar a Y:"+tmpIdVuelo);
            System.out.println("Fin de desencripcion");
            this.reloj ++;
            inst = new ClaseInstrucciones(this.identificador,  this.reloj,  1,  tmpIdVuelo);
            LOG.agregaInstruccion(inst);
            this.manejadorServidores.mandaInstrucciones(inst);
            
            
            this.y+= tmpIdVuelo;
            
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    @Override
    public byte[] getX(byte[] clientPubKeyEnc){
        byte[] tmp = ByteBuffer.allocate(4).putInt(this.x).array();
        try {
            return this.manejadorLlaves.encripta(clientPubKeyEnc, tmp);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return null;
    }

     @Override
    public byte[] getY(byte[] clientPubKeyEnc){
        byte[] tmp = ByteBuffer.allocate(4).putInt(this.y).array();
        try {
            return this.manejadorLlaves.encripta(clientPubKeyEnc, tmp);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return null;
    }

      

    @Override
    public void multiplicaX(byte[] idVuelo, int idLlave, byte[] clientPubKeyEnc, byte[] paramsEncriptClient) {
        ClaseInstrucciones inst;
        System.out.println("Operacion de suma recibida en el servidor");
        int tmpIdVuelo = 0;
        System.out.println("Parametro de entrada: ");
        System.out.println(idVuelo);
        try {
            tmpIdVuelo = ByteBuffer.wrap(this.manejadorLlaves.desencripta(clientPubKeyEnc, idVuelo, paramsEncriptClient)).getInt();
            System.out.println("Número a sumar a Y:"+tmpIdVuelo);
            System.out.println("Fin de desencripcion");
            this.reloj ++;
            inst = new ClaseInstrucciones(this.identificador,  this.reloj,  3,  tmpIdVuelo);
            LOG.agregaInstruccion(inst);
            
            this.manejadorServidores.mandaInstrucciones(inst);
            
            this.y *= tmpIdVuelo;
            
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    @Override
    public void multiplicaY(byte[] idVuelo, int idLlave, byte[] clientPubKeyEnc, byte[] paramsEncriptClient) {
        ClaseInstrucciones inst;
        System.out.println("Operacion de suma recibida en el servidor");
        int tmpIdVuelo = 0;
        System.out.println("Parametro de entrada: ");
        System.out.println(idVuelo);
        try {
            tmpIdVuelo = ByteBuffer.wrap(this.manejadorLlaves.desencripta(clientPubKeyEnc, idVuelo, paramsEncriptClient)).getInt();
            System.out.println("Número a sumar a Y:"+tmpIdVuelo);
            System.out.println("Fin de desencripcion");
            this.reloj ++;
            inst = new ClaseInstrucciones(this.identificador,  this.reloj,  4,  tmpIdVuelo);
            LOG.agregaInstruccion(inst);
            
            this.manejadorServidores.mandaInstrucciones(inst);this.y *= tmpIdVuelo;
            
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    public static void main(String args[]) throws SQLException, IOException {
        try {
            Server obj = new Server(206);
            Registry registry = LocateRegistry.createRegistry(1010);
            registry.bind("Hello", obj);
            System.out.println("Server RMI ready");
            
            SocketGeekForSeek manejadorSockets = new SocketGeekForSeek();
            manejadorSockets.start();
            
        } catch (AlreadyBoundException | RemoteException e) {
            System.err.println("Server exception no funcionó: " + e.toString());
            e.printStackTrace();
        }
    } 
    
    
    
   //******************************************
    //************************************
    //************************************
    //************************************
    
  public static class SocketGeekForSeek extends Thread
{     
    @Override
    public void run()  
    { 
        System.out.println("Server sockets ready");
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
        int time = 0;
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
                    
                    /**
                     * CUANDO LLEGA LA INSTRUCCION DE RELEASE
                     * 
                     * PREGUNTA AL NODO CABEZA SI EL IDENTIFICADOR ES IGUAL 
                     * AL QUE RECIBIÓ (TEORICAMENTE SÍ DEBERIA SERLO)
                     * 
                     * SACA LA INSTRUCCION DE LA COLA Y EJECUTA
                     * 
                     */
                    
                }else{
                    if(received.contains("reply")){
                        System.out.println("Solicitud de reply realizada");
                        
                        /**
                         * 
                         * 1. METE A SU HASH EL REPLY RECIBIDO
                         * 2. PREGUNTARLE A LA TABLA SI TIENE EL REPLY DE
                         *    TODOS LOS DEMAS.
                         * 
                         *    2.1.0 SI SÍ TIENE TODOS LOS REPLY
                         *    2.1.1 SE PREGUNTA SI EL ELEMENTO QUE SE ENCUENTRA
                         *          A LA CABEZA DEL LOG TIENE EL MISMO IDENTIFICADOR
                         *          QUE EL SERVIDOR DE FORMA LOCAL TIENE
                         *          
                         *          2.1.1.1 SI SÍ LO TIENE
                         *          2.1.1.1.1 SI SÍ LO TIENE EJECUTA LA INSTRUCCION DE FOMRA LOCAL, LIMPIA LA HASH TABLE Y MANDA EL RELEASE
                         * 
                         *              {"release":"release", "id":"xxx"}
                         * 
                         *          2.1.1.2 SI NO LO TIENE NO HACE NADA Y ESPERA EL RELEASE DEL NODO QUE SI TENGA AL INICIO LA INSTRICCION
                         *          
                         *           
                         *    2.1.0 SI NO TIENE TODOS LOS REPLY --> NO HACE NADA
                         * 
                         * 
                         */
                        
                        
                    }else{
                        jsonObject = new JSONObject(received);
                        time = Integer.parseInt(jsonObject.get("time").toString());
                        if(reloj > time){
                            time = reloj;
                        }else{
                            reloj = time;
                        }
                        ci = new ClaseInstrucciones(Integer.parseInt(jsonObject.get("sender").toString()), 
                                           time,
                                           jsonObject.get("action").toString()+jsonObject.get("target").toString(),
                                           Integer.parseInt(jsonObject.get("value").toString()));
                        LOG.agregaInstruccion(ci);
                        
                        /**
                         * Mandar el reply
                         * 
                         * {"reply":"reply", "id":"xxx"}
                         * 
                         */
                        
                        
                        //System.out.println("Estatus actual de la cola de prioridad");
                        //System.out.println(log.toString());
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
