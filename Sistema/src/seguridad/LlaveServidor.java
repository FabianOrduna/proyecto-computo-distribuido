package seguridad;

import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyAgreement;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author mcc06
 */
public class LlaveServidor {
    private int numeroCoordinacion;
    private byte[] llaveCliente;
    private String llavePrivada;
    private String llavePublica;
    //**********************
    private KeyPairGenerator aliceKpairGen;
    private KeyAgreement aliceKeyAgree;
    private KeyPair aliceKpair;
    private KeyFactory aliceKeyFac;
    private PublicKey bobPubKey;
    private SecretKeySpec aliceAesKey;
    private AlgorithmParameters aesParams;
    private Cipher aliceCipher;
    
    public LlaveServidor(int numeroCoordinacion) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException{
        this.numeroCoordinacion = numeroCoordinacion;
        this.llavePrivada = "";
        this.llaveCliente = null;
        
        /*
         * Alice creates her own DH key pair with 2048-bit key size
         */
        //System.out.println("ALICE: Generate DH keypair ...");
        this.aliceKpairGen = KeyPairGenerator.getInstance("DH");
        this.aliceKpairGen.initialize(2048);
        this.aliceKpair = aliceKpairGen.generateKeyPair();
        
        // Alice creates and initializes her DH KeyAgreement object
        //System.out.println("ALICE: Initialization ...");
        this.aliceKeyAgree = KeyAgreement.getInstance("DH");
        this.aliceKeyAgree.init(this.aliceKpair.getPrivate());
        
        //algoritmo empleado para la encodeada
        this.aesParams = AlgorithmParameters.getInstance("AES");
        this.aliceCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");  
    }
    
    public byte[] obtenLlaveInicial(){
        return aliceKpair.getPublic().getEncoded();
    }
    
    public void coordinaConCliente(byte[] bobPubKeyEnc) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException{
        /*
         * Alice uses Bob's public key for the first (and only) phase
         * of her version of the DH
         * protocol.
         * Before she can do so, she has to instantiate a DH public key
         * from Bob's encoded key material.
         */
        try{
            this.aliceKeyFac = KeyFactory.getInstance("DH");
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(bobPubKeyEnc);
            this.bobPubKey = aliceKeyFac.generatePublic(x509KeySpec); // ver a detalle que hace aqui ¿?


            //System.out.println("ALICE: Execute PHASE1 ...");
            this.aliceKeyAgree.doPhase(bobPubKey, true);
            this.generaLlaveSecreta(); 
            this.aliceCipher.init(Cipher.ENCRYPT_MODE, this.aliceAesKey);
            this.llaveCliente = bobPubKeyEnc;
        }catch(Exception e){
            System.out.println("Error en Llave servidor - coordina con cliente");
            System.out.println(e.toString());
        }
        
    }
    
    private byte[] generarSecreto(){
        byte [] tmp = aliceKeyAgree.generateSecret();
        //System.out.println("generate secret llave servidor");
        for(int i=0; i< tmp.length ; i++) {
         System.out.print(tmp[i] +" ");
        }
        return tmp;
    }
    
    private void generaLlaveSecreta(){
        this.aliceAesKey = new SecretKeySpec(this.generarSecreto(), 0, 16, "AES");
    }
    
    public byte[] encriptaMensaje(byte[] objetoEnBytes) throws IllegalBlockSizeException, BadPaddingException{
        
        try{
            byte [] tmp = aliceCipher.doFinal(objetoEnBytes);
            return tmp;
        }catch(Exception e){
            System.out.println("Encripta mensaje"); 
            System.out.println(e.toString());
        }
        return null;
    }
    
    public byte[] decriptaMensaje(byte[] objetoEncriptado, byte[] encodedParams) {
       
        try{
            aesParams.init(encodedParams);
            aliceCipher.init(Cipher.DECRYPT_MODE, aliceAesKey, aesParams);
        return aliceCipher.doFinal(objetoEncriptado);
        }catch(Exception e){
           System.out.println("Error Llave Cliente:");
           System.out.println(e.toString());
        }
        return null;
        
    }
    
    
    
    public byte[] obtenParametrosDeCifrado() throws IOException{
        return aliceCipher.getParameters().getEncoded();
    }
        
    public void guardaLlavePublicaCliente(byte[] llaveCliente){
        this.llaveCliente = llaveCliente;
    }
    
    public byte[] getLlavePublicaCliente(){
        return this.llaveCliente;
    }
    
    /*
     * Converts a byte to hex digit and writes to the supplied buffer
     */
    private static void byte2hex(byte b, StringBuffer buf) {
        char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
                '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        int high = ((b & 0xf0) >> 4);
        int low = (b & 0x0f);
        buf.append(hexChars[high]);
        buf.append(hexChars[low]);
    }

    /*
     * Converts a byte array to hex string
     */
    public static String toHexString(byte[] block) {
        StringBuffer buf = new StringBuffer();
        int len = block.length;
        for (int i = 0; i < len; i++) {
            byte2hex(block[i], buf);
            if (i < len-1) {
                buf.append(":");
            }
        }
        return buf.toString();
    }
}




        
        
        
