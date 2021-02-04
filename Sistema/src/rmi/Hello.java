/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

/**
 *
 * @author mcc06
 */
public interface Hello extends Remote{
    String sayHello(String persona) throws RemoteException;
    String sayHello() throws RemoteException;
    int insertaAlumno(String nombre, String paterno, String materno) throws RemoteException;
    int actualizaAlumno(int idAlumno, String nombre, String paterno, String materno) throws RemoteException;
}
