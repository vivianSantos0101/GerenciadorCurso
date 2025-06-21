/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package factory;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author olive
 */
public class ConnectionFactory {
    private static final String url = "jdbc:mysql://localhost:3306/gerenciadorcurso";
    private static final String usuario = "root";
    private static final String senha = "fatec";
  
   
    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(url, usuario, senha);
        } catch (SQLException e) {
            throw new SQLException("Erro ao conectar ao banco de dados!", e);
        }
    }

    
}
 