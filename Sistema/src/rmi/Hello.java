/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi;

import bd.Lugar;
import bd.Persona;
import bd.Vuelo;
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
    
    // Hello world - examples
    byte[] sayHello(String persona, int llaveId, byte[] clientPubKeyEnc)  throws RemoteException, SQLException, IOException;
    byte[] sayHello(int llaveId, byte[] clientPubKeyEnc) throws RemoteException, SQLException, IOException;
    byte[] insertaAlumno(String nombre, String paterno, String materno, int llaveId, byte[] clientPubKeyEnc) throws RemoteException, SQLException, IOException;
    byte[] actualizaAlumno(int idAlumno, String nombre, String paterno, String materno, int llaveId, byte[] clientPubKeyEnc) throws RemoteException, SQLException, IOException;

    // public byte[] enviarPrueba(byte[] clientPubKeyEnc) throws RemoteException, IOException, IllegalBlockSizeException, BadPaddingException;
    public byte[] obtenParametrosDeCifrado(int llaveId, byte[] clientPubKeyEnc) throws IOException;
    public byte[] vuelosHistoricos(int llaveId, byte[] clientPubKeyEnc) throws RemoteException, SQLException, IOException; //M
    public byte[] vuelosDisponibles(String fecha, int llaveId, byte[] clientPubKeyEnc) throws RemoteException, SQLException, IOException; //S
    public byte[] obtenerVuelo(int idVuelo, int llaveId, byte[] clientPubKeyEnc) throws RemoteException, SQLException, IOException; //F +
    public byte[] obtenerPersonasVuelo(int idVuelo, int llaveId, byte[] clientPubKeyEnc) throws RemoteException, SQLException, IOException; //M
    public byte[] obtenerPersonasVuelo(byte[] idVuelo, int llaveId, byte[] clientPubKeyEnc, byte[] paramsEncriptClient) throws RemoteException, SQLException, IOException; //M
    
    public byte[] obtenerPersonasVuelo(byte[] idVuelo, int llaveId, byte[] clientPubKeyEnc) throws RemoteException, SQLException, IOException; //M
    public byte[] vuelosHistoricosPersona(int idPersona, int llaveId, byte[] clientPubKeyEnc) throws RemoteException, SQLException, IOException; //S
    public byte[] vuelosDisponiblesPersona(String fecha, int idPersona, int llaveId, byte[] clientPubKeyEnc) throws RemoteException, SQLException, IOException; //F +
    public byte[] vuelosAnterioresPersona(String fecha, int idPersona, int llaveId, byte[] clientPubKeyEnc) throws RemoteException, SQLException, IOException; //M
    public byte[] obtenerLugares(int llaveId, byte[] clientPubKeyEnc) throws RemoteException, SQLException, IOException; //S
    public byte[] vuelosOrigenDestino(int idOrigen, int idDestino, int llaveId, byte[] clientPubKeyEnc) throws RemoteException, SQLException, IOException; //F

    
    
}
