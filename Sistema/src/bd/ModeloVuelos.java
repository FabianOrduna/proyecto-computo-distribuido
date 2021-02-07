/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author mcc06
 */
public class ModeloVuelos {
    private Connection c;
    
    public ModeloVuelos() throws SQLException{
        this.c = ConnectionPool.getInstance().getConnection();
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
