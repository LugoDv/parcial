/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controler;

import Connetion.ConexionMySQL;
import exceptions.NullConnectionException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class Product_controler {

    public ConexionMySQL conexion = new ConexionMySQL();

    public Product_controler() {
    
        this.conexion = new ConexionMySQL();
    }
    
    
    public Connection connect() { 
        Connection conn = conexion.conectarMySQL();//Al no estar este dentro de un try with resources sí se ejecuta el metodo .close()? habría que hacer un singleton de conexión?
        if (conn != null) {
            return conn;
        }
        throw new NullConnectionException();
    }
    
    
    public void Insert( String name, double price, String supplier,String final_date) { //paste: 
        String insertSQL = "INSERT INTO Products (name,price,supplier,final_date) VALUES (?,?,?,?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            
            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            pstmt.setString(3, supplier);
            pstmt.setString(4, final_date);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Successful insertion");
            } else {
                System.out.println("No insertion was made");
            }
        } catch (SQLException | NullConnectionException e) {
            System.out.println("An error occurred while connecting to database for data insertion");
            e.printStackTrace();
        }
    }
    
    public void select() {
        String selectSQL = "SELECT * FROM Products ";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.println("Id: " + rs.getInt("id") + ", name: " + rs.getString("name") + ", price: " + rs.getString("price") + ", supplier: " + rs.getString("supplier") + ", final_date " + rs.getString("final_date"));
            }

        } catch (SQLException | NullConnectionException e) {
            System.out.println("An error occurred while connecting to database for selection");
            e.printStackTrace();
        }
    }
    
    
}
