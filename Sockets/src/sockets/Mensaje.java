package sockets;
/**
 *
 * @author mcc06
 */
public class Mensaje implements Comparable{
    private int identificador;
    private int tiempo;
    private int instruccion;
    private int valor;
    public final static int NONE = 0;
    public final static int ADD_X = 1;
    public final static int ADD_Y = 2;
    public final static int MULTIPLY_X = 3;
    public final static int MULTIPLY_Y = 4;
    
    public Mensaje(int identificador, int tiempo, int instruccion, int value){
        this.identificador = identificador;
        this.tiempo = tiempo;
        this.instruccion = instruccion;
        this.valor = value;
    }
    
    public Mensaje(int identificador, int tiempo, String instruccion, int value){
        this.identificador = identificador;
        this.tiempo = tiempo;
        this.valor = value;
        instruccion = instruccion.toUpperCase();
        this.instruccion = NONE;
        switch(instruccion){
            case "ADDX":
                this.instruccion = ADD_X;
                break;
            case "ADDY":
                this.instruccion = ADD_Y;
                break;
            case "MULTIPLYX":
                this.instruccion = MULTIPLY_X;
                break;
            case "MULTIPLYY":
                this.instruccion = MULTIPLY_Y;
                break;
            default:
                this.instruccion = NONE;
                break;
        }
    }

    public int getIdentificador() {
        return identificador;
    }

    public int getTiempo() {
        return tiempo;
    }

    public int getInstruccion() {
        return instruccion;
    }

    public int getValor() {
        return valor;
    }
    
    
    
    @Override
    public String toString(){
        return "ID: "+this.identificador+"  T: "+this.tiempo+"  INS: "+this.instruccion + "VAL: "+this.valor;
    }

    @Override
    public int compareTo(Object t) {
        //este compare too es particular porque regresa unicamente si es mayor o
        //si es menor
        int res = -1;
        try{
            Mensaje externo = (Mensaje) t;
            if(externo.getTiempo() > this.tiempo){
                res = -1;
            }else{
                if(externo.getTiempo() < this.tiempo){
                    res = 1;
                }else{
                    if(externo.getIdentificador() > this.identificador){
                        res = -1;
                    }else{
                        res = 1;
                    }
                }
            }
        }catch(Exception e){
            res = -1;
        }
        return res;
    }
    
}