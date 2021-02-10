/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bd;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author mcc06
 */
public class ModeloVuelos {
    private Connection c;
    
    public ModeloVuelos() throws SQLException{
        //this.c = ConnectionPool.getInstance().getConnection();
    }
    
    public int insertaViajero(String nombre) throws SQLException{
        String sql = "INSERT INTO persona (nombre) VALUES (?)";
        PreparedStatement statement = c.prepareStatement(sql);
        statement.setString(1,nombre);
        return statement.executeUpdate();
    }
    
    public Vuelo obtenVuelo(int idVuelo) throws SQLException{
               
        String sql = "SELECT * FROM vuelo WHERE id_vuelo = ?";
        ResultSet resultado;
        try{
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setInt(1,idVuelo);
            resultado = statement.executeQuery();
            System.out.println(resultado.toString());
            resultado.next();
            //(int idVuelo, int idOrigen, int idDestino, String fecha)
            int resIdVuelo = Integer.parseInt(resultado.getString("id_vuelo"));
            int resIdOrigen = Integer.parseInt(resultado.getString("id_origen"));
            int resIdDestino = Integer.parseInt(resultado.getString("id_destino"));
            String resFecha = resultado.getString("fecha");
            return new Vuelo(resIdVuelo, resIdOrigen, resIdDestino, resFecha);
        }catch(SQLException e){
            System.out.println(e.toString());
            return null;
        }
    }
    
    public ArrayList<Vuelo> vuelosDisponiblesPersona(String fecha, int idPersona){
        ArrayList<Vuelo> resultados = new ArrayList();
        String sql = "SELECT * FROM vuelo WHERE fecha >= ? and id_vuelo in ( SELECT id_vuelo FROM persona_vuelo WHERE id_persona = ?)";
        ResultSet resultado;
        Vuelo v;
        try{
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setString(1,fecha);
            statement.setInt(2,idPersona);
            resultado = statement.executeQuery();
            System.out.println(resultado.toString());
            while(resultado.next()){
                int resIdVuelo = Integer.parseInt(resultado.getString("id_vuelo"));
                int resIdOrigen = Integer.parseInt(resultado.getString("id_origen"));
                int resIdDestino = Integer.parseInt(resultado.getString("id_destino"));
                String resFecha = resultado.getString("fecha");
                v= new Vuelo(resIdVuelo, resIdOrigen,resIdDestino, resFecha);
                resultados.add(v);    
            }
        }catch(SQLException e){
            System.out.println(e.toString());
            return resultados;
        }
        return resultados;
    }
    
    public ArrayList<Vuelo> vuelosOrigenDestino(int idOrigen, int idDestino){
        ArrayList<Vuelo> resultados = new ArrayList();
        String sql = "SELECT * FROM vuelo WHERE id_origen = ? and id_destino = ?";
        ResultSet resultado;
        Vuelo v;
        try{
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setInt(1,idOrigen);
            statement.setInt(2,idDestino);
            resultado = statement.executeQuery();
            System.out.println(resultado.toString());
            while(resultado.next()){
                int resIdVuelo = Integer.parseInt(resultado.getString("id_vuelo"));
                int resIdOrigen = Integer.parseInt(resultado.getString("id_origen"));
                int resIdDestino = Integer.parseInt(resultado.getString("id_destino"));
                String resFecha = resultado.getString("fecha");
                v= new Vuelo(resIdVuelo, resIdOrigen,resIdDestino, resFecha);
                resultados.add(v);    
            }
        }catch(SQLException e){
            System.out.println(e.toString());
            return resultados;
        }
        return resultados;
    }
    
    // Metodo que devuelve el historial de todos los vuelos
    // No recibe parametros
    // Devuelve un ArrayList de Vuelos
    public ArrayList<Vuelo> vuelosHistoricos(){
        ArrayList<Vuelo> resultados = new ArrayList();
        String sql = "SELECT * FROM vuelo";
        ResultSet resultado;
        Vuelo v;
        try{
            PreparedStatement statement = c.prepareStatement(sql);
            resultado = statement.executeQuery();
            System.out.println(resultado.toString());
            while(resultado.next()){
                int resIdVuelo = Integer.parseInt(resultado.getString("id_vuelo"));
                int resIdOrigen = Integer.parseInt(resultado.getString("id_origen"));
                int resIdDestino = Integer.parseInt(resultado.getString("id_destino"));
                String resFecha = resultado.getString("fecha");
                v= new Vuelo(resIdVuelo, resIdOrigen,resIdDestino, resFecha);
                resultados.add(v);    
            }
        }
        catch(Exception e){
            System.out.println(e.toString());
            return resultados;
        }
        return resultados;    
    }
    
    // Metodo que devuelve una tabla con las personas 
    // que volaran en un vuelo especifico
    // Recibe el identificador del vuelo
    // Devuelve un ArrayList de Personas
    public ArrayList<Persona> obtenerPersonasVuelo(int idVuelo){
        ArrayList<Persona> resultados = new ArrayList();
        String sql = "SELECT * FROM persona WHERE persona.id_persona in "
                + "(SELECT persona_vuelo.id_persona FROM persona_vuelo WHERE persona_vuelo.id_vuelo = ?)";
        ResultSet resultado;
        Persona p;
        try{
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setInt(1,idVuelo);
            resultado = statement.executeQuery();
            System.out.println(resultado.toString());
            while(resultado.next()){
                int resIdPersona = Integer.parseInt(resultado.getString("id_persona"));
                String resNombrePersona = resultado.getString("nombre");
                p= new Persona(resIdPersona, resNombrePersona);
                resultados.add(p);    
            }
        }
        catch(Exception e){
            System.out.println(e.toString());
            return resultados;
        }
        return resultados;
    }
    
    // Metodo que devuelve los vuelos que ocurrieron antes de una fecha especifica
    // en el historial de una persona especifica
    // Recibe la fecha en formato YYYY-MM-DD y el identificador de la Persona
    // Devuelve un ArrayList de Vuelos
    public ArrayList<Vuelo> vuelosAnterioresPersona(String fecha, int idPersona){
        ArrayList<Vuelo> resultados = new ArrayList();
        String sql = "SELECT DISTINCT persona.id_persona, persona.nombre, vuelo.id_vuelo, vuelo.fecha,"
                + "(SELECT lugar.nombre FROM lugar WHERE lugar.id_lugar = vuelo.id_origen) as origen,"
                + "(SELECT lugar.nombre FROM lugar WHERE lugar.id_lugar = vuelo.id_destino) as destino"
                + "FROM persona INNER JOIN persona_vuelo ON persona_vuelo.id_persona = persona.id_persona"
                + "INNER JOIN vuelo ON vuelo.id_vuelo = persona_vuelo.id_vuelo WHERE persona.id_persona = ?"
                + "AND vuelo.fecha <= ?";
        ResultSet resultado;
        Vuelo v;
        try{
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setInt(1,idPersona);
            statement.setString(2,fecha);
            resultado = statement.executeQuery();
            System.out.println(resultado.toString());
            while(resultado.next()){
                int resIdVuelo = Integer.parseInt(resultado.getString("id_vuelo"));
                int resIdOrigen = Integer.parseInt(resultado.getString("id_origen"));
                int resIdDestino = Integer.parseInt(resultado.getString("id_destino"));
                String resFecha = resultado.getString("fecha");
                v= new Vuelo(resIdVuelo, resIdOrigen,resIdDestino, resFecha);
                resultados.add(v);     
            }
        }
        catch(Exception e){
            System.out.println(e.toString());
            return resultados;
        }
        return resultados;
    }
    
    /**
    
    * De Mariana Hernandez para todos:  01:48 PM
try {
            conexion = DriverManager.getConnection(url, usuarios, clave);
            stmt = conexion.createStatement();
            stmt.execute("INSERT INTO usuarios VALUES(null, 'Tomasa', 'tomasa@mail.com', '34567')");
            resultado = stmt.executeQuery("SELECT * FROM usuarios");
            resultado.next();
            do{
                System.out.println(resultado.getString("id_usuario")+ 
                        " Nombre: "+resultado.getString("nombre")+ 
                        " Correo: "+resultado.getString("correo")+
                        " Contraseña: "+resultado.getString("contrasenia"));
            }while(resultado.next());
        } catch (SQLException ex) {
            Logger.getLogger(Sql.class.getName()).log(Level.SEVERE, null, ex);
        }
String usuarios = "root";
        String clave = "";
        String url = "jdbc:mysql://localhost:3306/prueba_db";
        Connection conexion;
        Statement stmt;
        ResultSet resultado;

     
    
    
    * 
     */
    
  
}
