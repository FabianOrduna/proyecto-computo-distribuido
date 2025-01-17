/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seguridad;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author mcc06
 */
public class ManejadorLlaves {
    private ArrayList<LlaveServidor> manejadorLlaves;
    private int totalCreados;
    
    public ManejadorLlaves(){
        this.manejadorLlaves = new ArrayList<LlaveServidor>();
        this.totalCreados = 0;
        this.manejadorLlaves.add(0,null);
    }
    
    /**
     * Este método se emplea para crear un nuevo objeto de llave privada,
     * de forma que para cada nuevo cliente sea lo primero que se hace y puedan
     * llevarse a cabo los siguientes pasos de coordinación
     * @return indice del arraylist donde se encuentra la llave indicada
     */
    public int crearLlave(){
        this.totalCreados++;
        try{
            LlaveServidor ll = new LlaveServidor(this.totalCreados);
            this.manejadorLlaves.add(this.totalCreados,ll);
            return this.totalCreados;
            
        }catch(Exception e){
            System.out.println(e.toString());
            return 0;
        }
    }
    
    public byte[] obtenerLlave(int idLlaveCliente){
        if(this.manejadorLlaves.get(idLlaveCliente)!=null){
            return this.manejadorLlaves.get(idLlaveCliente).obtenLlaveInicial();
        }else{
            return null;
        }   
    }
    
    public byte[] obtenParametrosDeCifrado(int idLlaveCliente) throws IOException{
        if(this.manejadorLlaves.get(idLlaveCliente)!=null){
            return this.manejadorLlaves.get(idLlaveCliente).obtenParametrosDeCifrado();
        }else{
            return null;
        }   
    }
    
    public boolean coordLlave(int idLlaveCliente, byte[] llavePublicaCliente){
        if(this.manejadorLlaves.get(idLlaveCliente)!=null){
            try {
                this.manejadorLlaves.get(idLlaveCliente).coordinaConCliente(llavePublicaCliente);
                return true;
            } catch (Exception ex) {
                System.out.println("Error al coordinar la llave del cliente: "+idLlaveCliente);
            }   
        }
        return false;
    }
    
    public byte[] encripta(byte[] llavePublicaCliente, byte[] cosa) throws IllegalBlockSizeException, BadPaddingException{
        //System.out.println("encriptando en el servidor con la llave");
        return encuentraLlave(llavePublicaCliente).encriptaMensaje(cosa);
    }
    
    public byte[] desencripta(byte[] llavePublicaCliente, byte[] cosa, byte[] paramsEncriptadoCliente) throws IllegalBlockSizeException, BadPaddingException{
        //System.out.println("encriptando en el servidor con la llave");
        return encuentraLlave(llavePublicaCliente).decriptaMensaje(cosa, paramsEncriptadoCliente);
    }
    
    private LlaveServidor encuentraLlave(byte[] llavePublica){
        
        LlaveServidor temp = null;
        int indice = 1;
        boolean encontrado = false;
        while(indice < this.manejadorLlaves.size() && !encontrado){
            temp = this.manejadorLlaves.get(indice);

            if(Arrays.equals(temp.getLlavePublicaCliente(), llavePublica)){
                encontrado=true;
                //System.out.println("Llave encontrada es la: "+indice);
            }
            indice++;
        }
        //System.out.println(temp.toString());
        return temp;
    }
    
    
}
