/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seguridad;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.KeyAgreement;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;

/**
 *
 * @author mcc06
 */
public class ManejadorSeguridadClient {
    /*
    * Let's turn over to Bob. Bob has received Alice's public key
    * in encoded format.
    * He instantiates a DH public key from the encoded key material.
    */
    private KeyFactory bobKeyFac;
    private X509EncodedKeySpec x509KeySpec;
    private KeyPairGenerator bobKpairGen;
    private KeyPair bobKpair;
    private KeyAgreement bobKeyAgree;

    public ManejadorSeguridadClient(byte[]alicePubKeyEnc) throws NoSuchAlgorithmException{
       this.bobKeyFac = KeyFactory.getInstance("DH");
       this.x509KeySpec = new X509EncodedKeySpec(alicePubKeyEnc);
       this.bobKpairGen = null;
    }

    public PublicKey generatePublicKey() throws InvalidKeySpecException{
       return bobKeyFac.generatePublic(this.x509KeySpec);
    }
    
    public void crearPublicKey(PublicKey publicKey) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException{
        DHParameterSpec dhParamFromAlicePubKey = ((DHPublicKey)publicKey).getParams();
        // Bob creates his own DH key pair
        this.setBobKpairGen(KeyPairGenerator.getInstance("DH"));
        bobKpairGen.initialize(dhParamFromAlicePubKey);
        this.bobKpair = bobKpairGen.generateKeyPair();
    }
    
    public byte[] obtenerLlavePublica(){
        return bobKpair.getPublic().getEncoded();
    }
    
    public void phaseBob(PublicKey alicePubKey) throws InvalidKeyException{
        this.bobKeyAgree.doPhase(alicePubKey, true);
    }

    public KeyFactory getBobKeyFac() {
        return bobKeyFac;
    }

    public void setBobKeyFac(KeyFactory bobKeyFac) {
        this.bobKeyFac = bobKeyFac;
    }

    public X509EncodedKeySpec getX509KeySpec() {
        return x509KeySpec;
    }

    public void setX509KeySpec(X509EncodedKeySpec x509KeySpec) {
        this.x509KeySpec = x509KeySpec;
    }

    public KeyPairGenerator getBobKpairGen() {
        return bobKpairGen;
    }

    public void setBobKpairGen(KeyPairGenerator bobKpairGen) {
        this.bobKpairGen = bobKpairGen;
    }
        
       
       
          
        
}
