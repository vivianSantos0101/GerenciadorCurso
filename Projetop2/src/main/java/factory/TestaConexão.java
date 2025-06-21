/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package factory;

import java.sql.Connection;
import java.sql.SQLException;


public class TestaConexão {

    
    public static void main(String[] args) throws SQLException {
       Connection connection = new ConnectionFactory().getConnection();
        System.out.println("Conexão bem sucedida!");
        connection.close();
    }
    
}

