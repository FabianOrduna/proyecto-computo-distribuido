/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seguridad;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
import javax.crypto.ShortBufferException;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author mcc06
 */
public class Cliente {
    public byte[] llaveInicialServidor;
    //****
    private KeyFactory bobKeyFac;
    private PublicKey alicePubKey;
    private DHParameterSpec dhParamFromAlicePubKey;
    private KeyPairGenerator bobKpairGen;
    private KeyAgreement bobKeyAgree;
    private KeyPair bobKpair;
    private SecretKeySpec bobAesKey;
    private Cipher bobCipher;
    private AlgorithmParameters aesParams;
    
    
    
    public Cliente(byte[] llaveServidor) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException, InvalidKeyException, NoSuchPaddingException{
        //System.out.println("Dentro de la clase con FOF");
        this.llaveInicialServidor = llaveServidor;
        /*
         * Let's turn over to Bob. Bob has received Alice's public key
         * in encoded format.
         * He instantiates a DH public key from the encoded key material.
         */
        this.bobKeyFac = KeyFactory.getInstance("DH");
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(llaveServidor);  
        this.alicePubKey = bobKeyFac.generatePublic(x509KeySpec);
        /*
         * Bob gets the DH parameters associated with Alice's public key.
         * He must use the same parameters when he generates his own key
         * pair.
         */
        this.dhParamFromAlicePubKey = ((DHPublicKey)this.alicePubKey).getParams();
        // Bob creates his own DH key pair
        System.out.println("BOB: Generate DH keypair ...");
        this.bobKpairGen = KeyPairGenerator.getInstance("DH");
        bobKpairGen.initialize(dhParamFromAlicePubKey);
        this.bobKpair = bobKpairGen.generateKeyPair();
        // Bob creates and initializes his DH KeyAgreement object
        System.out.println("BOB: Initialization ...");
        this.bobKeyAgree = KeyAgreement.getInstance("DH");
        this.bobKeyAgree.init(bobKpair.getPrivate());
        /*
         * Bob encrypts, using AES in CBC mode
         */
        this.aesParams = AlgorithmParameters.getInstance("AES");
        this.bobCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");  
    }
    
    // Bob encodes his public key, and sends it over to Alice.
    public byte[] obtenLlave(){
        return this.bobKpair.getPublic().getEncoded();
    }
    
    public void coordinaConServidor() throws InvalidKeyException{
        /*
         * Bob uses Alice's public key for the first (and only) phase
         * of his version of the DH
         * protocol.
         */
        System.out.println("BOB: Execute PHASE1 ...");
        bobKeyAgree.doPhase(this.alicePubKey, true);
        this.generaLlaveSecreta();
        this.bobCipher.init(Cipher.ENCRYPT_MODE, this.bobAesKey);
    }
    
    public int longitudSecreto(byte[] bobSharedSecret) throws IllegalStateException, ShortBufferException{
        return bobKeyAgree.generateSecret(bobSharedSecret, 0);
    }
    
    private byte[] generarSecreto(){
        return bobKeyAgree.generateSecret();
    }
    
    private void generaLlaveSecreta(){
        this.bobAesKey = new SecretKeySpec(this.generarSecreto(), 0, 16, "AES");
    }
    
    public byte[] encriptaMensaje(byte[] objetoEnBytes) throws IllegalBlockSizeException, BadPaddingException {
       return bobCipher.doFinal(objetoEnBytes);
    }
    
    public byte[] obtenParametrosDeCifrado() throws IOException{
        return bobCipher.getParameters().getEncoded();
    }
    
    public byte[] decriptaMensaje(byte[] objetoEncriptado, byte[] encodedParams) throws NoSuchAlgorithmException, IOException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
       
        aesParams.init(encodedParams);
        bobCipher.init(Cipher.DECRYPT_MODE, bobAesKey, aesParams);
        return bobCipher.doFinal(objetoEncriptado);
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
    private static String toHexString(byte[] block) {
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
