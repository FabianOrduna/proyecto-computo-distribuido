package rmi;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
import java.util.Map;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import org.json.JSONException;
import org.json.JSONObject;
import seguridad.ManejadorLlaves;
import sockets.Mensaje;
import sockets.ColaDePrioridad;
import sockets.ManejadorSockets;
import sockets.Nodo;

public class Server extends UnicastRemoteObject implements Hello {
    
    private ManejadorLlaves manejadorLlaves;
    private static int reloj;
    private static int x;
    private static int y;
    public static ColaDePrioridad LOG = new ColaDePrioridad();
    private static int identificador;
    public static ManejadorSockets manejadorServidores;
    public static final int NUM_NODOS = 3; //esto incluye al nodo actual
    public static final int NUM_VECINOS = 2;
    public static Map <Integer, Integer> replyTable = new Hashtable();
    
    
    public Server(int identificador, int idsVecinos[], Nodo[] losVecinos) throws RemoteException, SQLException, AlreadyBoundException, IOException{

        this.manejadorLlaves = new ManejadorLlaves();
        Server.reloj = 1;
        Server.x = 0;
        Server.y = 0;
        Server.identificador = identificador;
        Server.manejadorServidores = new ManejadorSockets(losVecinos);
        
        for (int i = 0; i < idsVecinos.length; i++) {
            replyTable.put(idsVecinos[i],0 );
        }
        
    }
    
    public byte[] serializa(Object objeto) { 
        try{
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(objeto);
        byte[] cleartext = out.toByteArray();
        return cleartext;
        }catch(IOException e){
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
        Mensaje inst;
        //System.out.println("Operacion de suma recibida en el servidor");
        int tmpIdVuelo;
        //System.out.println("Parametro de entrada: ");
        //System.out.println(idVuelo);
        try {
            tmpIdVuelo = ByteBuffer.wrap(this.manejadorLlaves.desencripta(clientPubKeyEnc, idVuelo, paramsEncriptClient)).getInt();
           // System.out.println("Número a sumar a X:"+tmpIdVuelo);
            //System.out.println("Fin de desencripcion");
            Server.reloj ++;
            inst = new Mensaje(Server.identificador,  Server.reloj,  1,  tmpIdVuelo);
            LOG.agregaInstruccion(inst);
            
            Server.manejadorServidores.mandaInstrucciones(inst);
            
            //this.x+=tmpIdVuelo;
            
        } catch (IOException | BadPaddingException | IllegalBlockSizeException ex) {
            System.out.println(ex.toString());
        }
    }
    
    @Override
    public void sumaY(byte[] idVuelo, int llaveId, byte[] clientPubKeyEnc, byte[] paramsEncriptClient) throws RemoteException, SQLException, IOException {
        Mensaje inst;
        //System.out.println("Operacion de suma recibida en el servidor");
        int tmpIdVuelo;
        //System.out.println("Parametro de entrada: ");
        //System.out.println(idVuelo);
        try {
            tmpIdVuelo = ByteBuffer.wrap(this.manejadorLlaves.desencripta(clientPubKeyEnc, idVuelo, paramsEncriptClient)).getInt();
            //System.out.println("Número a sumar a Y:"+tmpIdVuelo);
            //System.out.println("Fin de desencripcion");
            Server.reloj ++;
            inst = new Mensaje(Server.identificador,  Server.reloj,  2,  tmpIdVuelo);
            LOG.agregaInstruccion(inst);
            Server.manejadorServidores.mandaInstrucciones(inst);
            
            
            //this.y+= tmpIdVuelo;
            
        } catch (IOException | BadPaddingException | IllegalBlockSizeException ex) {
            System.out.println(ex.toString());
        }
    }

    @Override
    public byte[] getX(byte[] clientPubKeyEnc){
        byte[] tmp = ByteBuffer.allocate(4).putInt(Server.x).array();
        try {
            return this.manejadorLlaves.encripta(clientPubKeyEnc, tmp);
        } catch (BadPaddingException | IllegalBlockSizeException ex) {
            System.out.println(ex.toString());
        }
        return null;
    }

     @Override
    public byte[] getY(byte[] clientPubKeyEnc){
        byte[] tmp = ByteBuffer.allocate(4).putInt(Server.y).array();
        try {
            return this.manejadorLlaves.encripta(clientPubKeyEnc, tmp);
        } catch (BadPaddingException | IllegalBlockSizeException ex) {
            System.out.println(ex.toString());
        }
        return null;
    }

      

    @Override
    public void multiplicaX(byte[] idVuelo, int idLlave, byte[] clientPubKeyEnc, byte[] paramsEncriptClient) {
        Mensaje inst;
        //System.out.println("Operacion de suma recibida en el servidor");
        int tmpIdVuelo;
        //System.out.println("Parametro de entrada: ");
        //System.out.println(idVuelo);
        try {
            tmpIdVuelo = ByteBuffer.wrap(this.manejadorLlaves.desencripta(clientPubKeyEnc, idVuelo, paramsEncriptClient)).getInt();
            //System.out.println("Número a sumar a Y:"+tmpIdVuelo);
            //System.out.println("Fin de desencripcion");
            Server.reloj ++;
            inst = new Mensaje(Server.identificador,  Server.reloj,  3,  tmpIdVuelo);
            LOG.agregaInstruccion(inst);
            
            Server.manejadorServidores.mandaInstrucciones(inst);
            
            //this.x *= tmpIdVuelo;
            
        } catch (IOException | BadPaddingException | IllegalBlockSizeException ex) {
            System.out.println(ex.toString());
        }
    }

    @Override
    public void multiplicaY(byte[] idVuelo, int idLlave, byte[] clientPubKeyEnc, byte[] paramsEncriptClient) {
        Mensaje inst;
        //System.out.println("Operacion de suma recibida en el servidor");
        int tmpIdVuelo;
        //System.out.println("Parametro de entrada: ");
        //System.out.println(idVuelo);
        try {
            tmpIdVuelo = ByteBuffer.wrap(this.manejadorLlaves.desencripta(clientPubKeyEnc, idVuelo, paramsEncriptClient)).getInt();
            //System.out.println("Número a sumar a Y:"+tmpIdVuelo);
            //System.out.println("Fin de desencripcion");
            Server.reloj ++;
            inst = new Mensaje(Server.identificador,  Server.reloj,  4,  tmpIdVuelo);
            LOG.agregaInstruccion(inst);
            
            Server.manejadorServidores.mandaInstrucciones(inst);Server.y *= tmpIdVuelo;
            //this.y *= tmpIdVuelo;
            
        } catch (IOException | BadPaddingException | IllegalBlockSizeException ex) {
            System.out.println(ex.toString());
        }
    }

    
    
    
    
    
    
    public static boolean ejecutaInstruccion(Mensaje inst){
        try{
            System.out.println("Se ejecuta la instruccion :"+inst.toString());
            /*NONE = 0;
            ADD_X = 1;
            ADD_Y = 2;
            MULTIPLY_X = 3;
            MULTIPLY_Y = 4;*/
            
            switch(inst.getInstruccion()){
                case 1:
                    x+=inst.getValor();
                    break;
                case 2:
                    y+=inst.getValor();
                    break;
                case 3:
                    x*=inst.getValor();
                    break;
                case 4:
                    y*=inst.getValor();
                    break;
                default:
                    System.out.println("Instruccion no encontrada");
                    break;
            }
            
            
            
            return true;
        }catch(Exception e){
            System.out.println("Error en ejecuta instrucciones");
            return false;
        }
    }
    
    
    
    
    
    
    
    
    public static void main(String args[]) throws SQLException, IOException {
        try {
            int vecinos[] = {203};//cambiar al ejecutar
            int miId = 206; //cambiar al ejecutar
            
            Nodo n1,n2,n3;
            //n1 = new Nodo("148.205.36.218",5056, 218, false);
            //n2 = new Nodo("148.205.36.214",5056, 214);
            n2 = new Nodo("148.205.36.203",3000, 203, false);//brandon
            //n1 = new Nodo("148.205.36.210",9000, 210);//braulio
            //n1 = new Nodo("148.205.36.205",5000, 205);//pedro
            //n2 = new Nodo("148.205.36.215",15000, 215);//diego
            //n1 = new Nodo("148.205.36.207",5000, 207);//julio
            
           // Nodo[] losVecinosNodos = {n1,n2,n3};
            Nodo[] losVecinosNodos = {n2};
            //Nodo[] losVecinosNodos = {n1};
            
            
            Server obj = new Server(miId, vecinos,losVecinosNodos);
            Registry registry = LocateRegistry.createRegistry(1010);
            registry.bind("Hello", obj);
            System.out.println("Server RMI ready");
            
            ServidorSockets manejadorSockets = new ServidorSockets();
            manejadorSockets.start();
            
        } catch (AlreadyBoundException | RemoteException e) {
            System.err.println("Server exception no funcionó: " + e.toString());
        }
    } 
    
    
    
   //******************************************
    //****************************************
    //****************************************
    //****************************************
    
  public static class ServidorSockets extends Thread
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
                    DataInputStream dis = new DataInputStream(new BufferedInputStream(s.getInputStream()));
                    DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                    
                    //System.out.println("Assigning new thread for this client");
                    
                    // create a new thread object
                    Thread t = new ManejadorClientes(s, dis, dos);
                    
                    // Invoking the start() method
                    t.start();
                    
                }
                catch (IOException e){
                    s.close();
                } 
            }
        } catch (IOException ex) {
            //Logger.getLogger(ServidorSockets.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
} 

// ClientHandler class 
static class ManejadorClientes extends Thread  
{ 
    
    final DataInputStream dis; 
    final DataOutputStream dos; 
    final Socket s; 
    BufferedReader reader;
    
  
    // Constructor 
    public ManejadorClientes(Socket s, DataInputStream dis, DataOutputStream dos) throws IOException
    { 
        this.s = s; 
        this.dis = dis; 
        this.dos = dos;
        this.reader = new BufferedReader(new InputStreamReader(s.getInputStream())); 
        //this.d = new BufferedReader(new InputStreamReader(s));
    } 
  
    @Override
    public void run()  
    { 
        String received;
        JSONObject jsonObject;
        Mensaje ci;
        int time;
        int entero;
        boolean unosReplyTable;
        while (true)  
        {   
            unosReplyTable = true;
            
            
            try { 
  
                // Ask user what he wants 
                /*dos.writeUTF("What do you want?[Sumar | Multiplicar]..\n"+ 
                            "Type Exit to terminate connection.");
*/
                  
                // receive the answer from client 
                //received = dis.readUTF();
                //received = dis.readLine();
                received = reader.readLine();
                if(received!=null){
                    System.out.println("Leer -->");
                    System.out.println("Después de leer");
                    System.out.println("Lo que quiere el cliente"+s.toString()+" es: ");
                    System.out.println(received);
                }
                
                //System.out.println("Valor actual de la variable: "+ArribaConRMI.numero);
                if(received == null){
                    //System.out.println("Se recibe algo nulo");
                }
                
                
                if(received!= null && received.equals("Exit")) 
                {  
                    System.out.println("Client " + this.s + " sends exit..."); 
                    //System.out.println("Closing this connection."); 
                    this.s.close(); 
                    //System.out.println("Connection closed"); 
                    break; 
                } 
                
                if(received!= null && received.contains("release")){
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
                    System.out.println("Llegó el release, qué emotivo");
                    System.out.println("release");
                    jsonObject = new JSONObject(received);
                    entero = Integer.parseInt(jsonObject.get("id").toString());
                    
                    if(LOG.getCabeza().getIdentificador() == entero){
                        ejecutaInstruccion(LOG.pollCabeza());
                        for(int indice : replyTable.keySet()){                 
                            replyTable.put(indice,0 );
                        }
                    }
                    
                    
                }else{
                    if(received!= null && received.contains("reply")){
                        
                        System.out.println("Solicitud de reply realizada");
                        
                        System.out.println("Reply entrante:");
                        System.out.println(received);
                        
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
                        
                        jsonObject = new JSONObject(received);
                        entero = Integer.parseInt(jsonObject.get("id").toString());
                        
                        replyTable.put(entero ,1);
                        
                        
                        for(int indice : replyTable.keySet()){
                            //System.out.println("Adento del foreach");
                            unosReplyTable = unosReplyTable && replyTable.get(indice) == 1;
                            if(unosReplyTable == false){
                                break;
                            }
                        }
                        
                        System.out.println(replyTable.toString());
                        System.out.println("Tiene todos los reply?" + unosReplyTable);
                        
                        if(unosReplyTable){
                            if(LOG.getCabeza().getIdentificador() == identificador){
                                //System.out.println("ejecuto por unos");
                                
                                ejecutaInstruccion(LOG.pollCabeza());
                               
                                String mensajeRelease = "{\"release\":\"release\", \"id\":\""+identificador+"\"}\n";
                        
                                manejadorServidores.mandaReleaseATodos(mensajeRelease);
                        
                                
                                
                            }else{
                                System.out.println("A llorar... no me toca :(");
                            }
                        }
                        
                   

                        
                        
                        
                        
                        
                    }else{
                        if(received!= null){
                            jsonObject = new JSONObject(received);
                            time = Integer.parseInt(jsonObject.get("time").toString());
                            if(reloj > time){
                                time = reloj;
                            }else{
                                reloj = time;
                            }
                            System.out.println(received);
                            ci = new Mensaje(Integer.parseInt(jsonObject.get("sender").toString()), 
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

                            String mensajeReply = "{\"reply\":\"reply\", \"id\":\""+identificador+"\"}\n";

                            manejadorServidores.mandaJSONANodoPorIdentificador(Integer.parseInt(jsonObject.get("sender").toString()), mensajeReply);

                            //System.out.println("Estatus actual de la cola de prioridad");
                            //System.out.println(log.toString());
                        }
                    }
                }
                 
            } catch (IOException | JSONException  e) { 
                System.out.println(e.toString());
                
            }
            //Logger.getLogger(ArribaConRMI.class.getName()).log(Level.SEVERE, null, ex);
            
            
            
        } 
          
        try
        { 
            // closing resources 
            this.dis.close(); 
            this.dos.close(); 
              
        }catch(IOException e){ 
        } 
    } 
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
