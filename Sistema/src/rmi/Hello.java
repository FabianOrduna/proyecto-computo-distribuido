/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi;

import bd.Lugar;
import bd.Persona;
import bd.Vuelo;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author mcc06
 */
public interface Hello extends Remote{
    String sayHello(String persona) throws RemoteException;
    String sayHello() throws RemoteException;
    int insertaAlumno(String nombre, String paterno, String materno) throws RemoteException;
    int actualizaAlumno(int idAlumno, String nombre, String paterno, String materno) throws RemoteException;
      
    public ArrayList<Vuelo> vuelosHistoricos() throws RemoteException; //M
    public ArrayList<Vuelo> vuelosDisponibles(String fecha) throws RemoteException; //S
    public Vuelo obtenerVuelo(int idVuelo); //F
    public ArrayList<Persona> obtenerPersonasVuelo(int idVuelo); //M
    public ArrayList<Vuelo> vuelosHistoricosPersona(int idPersona) throws RemoteException; //S
    public ArrayList<Vuelo> vuelosDisponiblesPersona(String fecha, int idPersona) throws RemoteException; //F
    public ArrayList<Vuelo> vuelosAnterioresPersona(String fecha, int idPersona) throws RemoteException; //M
    public ArrayList<Lugar> obtenerLugares() throws RemoteException; //S
    public ArrayList<Vuelo> vuelosOrigenDestino(int idOrigen, int idDestino) throws RemoteException; //F
    
}
