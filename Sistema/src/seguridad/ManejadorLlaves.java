/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seguridad;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    }
    
    /**
     * Este método se emplea para crear un nuevo objeto de llave privada,
     * de forma que para cada nuevo cliente sea lo primero que se hace y puedan
     * llevarse a cabo los siguientes pasos de coordinación
     * @return indice del arraylist donde se encuentra la llave indicada
     */
    public int obtenLlavePublica(){
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
    
    public byte[] obtenerLlavePublica(int idLlaveCliente){
        if(this.manejadorLlaves.get(idLlaveCliente)!=null){
            return this.manejadorLlaves.get(idLlaveCliente).obtenLlaveInicial();
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
    
}
