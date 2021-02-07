/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 * https://www.codejava.net/java-se/jdbc/jdbc-tutorial-sql-insert-select-update-and-delete-examples
 * @author mcc06
 */
public class ModeloAlumno {
    
    private Connection c;
    
    public ModeloAlumno() throws SQLException{
        this.c = ConnectionPool.getInstance().getConnection();
    }
    
    public int insertaAlumno(String nombre, String paterno, String materno) throws SQLException{
        String sql = "INSERT INTO alumno (nombre, paterno, materno) VALUES (?, ?, ?)";
        PreparedStatement statement = c.prepareStatement(sql);
        statement.setString(1,nombre);
        statement.setString(2,paterno);
        statement.setString(3,materno);
        return statement.executeUpdate();
    }
    
    public int actualizaAlumno(int idAlumno, String nombre, String paterno, String materno) throws SQLException{
        String sql = "UPDATE alumno SET nombre = ?, paterno = ? , materno=? WHERE id_alumno = ?";
        PreparedStatement statement = c.prepareStatement(sql);
        statement.setString(1,nombre);
        statement.setString(2,paterno);
        statement.setString(3,materno);
        statement.setInt(4,idAlumno);
        return statement.executeUpdate();
    }
    
    
    
}
