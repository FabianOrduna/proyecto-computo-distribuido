/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pruebasockets;


import java.util.PriorityQueue;

/**
 *
 * @author mcc06
 */
public class ColaDePrioridad {
    
    PriorityQueue<ClaseInstrucciones> pq;

    public ColaDePrioridad() {
        pq = new PriorityQueue();
    }
    
    public boolean agregaInstruccion(ClaseInstrucciones inst){
        return pq.add(inst);
    }
    
    public ClaseInstrucciones sacaInstruccion(){
        return pq.poll();
    }
    
    public String toString(){
        return pq.toString();
    }
    
}
