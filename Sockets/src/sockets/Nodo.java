/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sockets;

/**
 *
 * @author mcc06
 */
public class Nodo {
    private String host;
    private int port;
    private int id;
    private boolean mantenerAbierto;

    public Nodo(String host, int port, int id) {
        this.host = host;
        this.port = port;
        this.id = id;
        this.mantenerAbierto = true;
    }
    
    public Nodo(String host, int port, int id, boolean mantenerAbierto) {
        this.host = host;
        this.port = port;
        this.id = id;
        this.mantenerAbierto = mantenerAbierto;
    }
    
    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public int getId() {
        return id;
    }

    public boolean isMantenerAbierto() {
        return mantenerAbierto;
    }
    
    
    
}
