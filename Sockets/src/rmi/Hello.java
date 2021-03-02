/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

/**
 *
 * @author mcc06
 */
public interface Hello extends Remote{
    
    public int crearLlave() throws RemoteException; //M
    public byte[] obtenLlave(int llaveId) throws RemoteException; //M
    public void coordLlave(int llaveId, byte[] clientPubKeyEnc) throws RemoteException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException;
    public byte[] obtenParametrosDeCifrado(int llaveId, byte[] clientPubKeyEnc) throws IOException;
    
    //método
    public void sumaX(byte[] idVuelo, int llaveId, byte[] clientPubKeyEnc, byte[] paramsEncriptClient) throws RemoteException, SQLException, IOException; //M
    public void sumaY(byte[] idVuelo, int llaveId, byte[] clientPubKeyEnc, byte[] paramsEncriptClient) throws RemoteException, SQLException, IOException; //M
    
    public byte[] getX(byte[] clientPubKeyEnc) throws RemoteException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException;
    public byte[] getY(byte[] clientPubKeyEnc) throws RemoteException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException;
    
}
