/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bd;

import java.io.Serializable;
/**
 *
 * @author mcc06
 */
public class Vuelo implements Serializable{
    private int idVuelo;
    private int idOrigen;
    private int idDestino;
    private String fecha;

    public Vuelo(int idVuelo, int idOrigen, int idDestino, String fecha) {
        this.idVuelo = idVuelo;
        this.idOrigen = idOrigen;
        this.idDestino = idDestino;
        this.fecha = fecha;
    }

    public int getIdVuelo() {
        return idVuelo;
    }

    public void setIdVuelo(int idVuelo) {
        this.idVuelo = idVuelo;
    }

    public int getIdOrigen() {
        return idOrigen;
    }

    public void setIdOrigen(int idOrigen) {
        this.idOrigen = idOrigen;
    }

    public int getIdDestino() {
        return idDestino;
    }

    public void setIdDestino(int idDestino) {
        this.idDestino = idDestino;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Vuelo{" + "idVuelo=" + idVuelo + ", idOrigen=" + idOrigen + ", idDestino=" + idDestino + ", fecha=" + fecha + '}';
    }
    
}
