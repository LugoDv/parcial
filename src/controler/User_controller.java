/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controler;

/**
 *
 * @author lugo
 */
import Connetion.ConexionMySQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User_controller {

    // Creamos un objeto de la clase ConexionMySQL
    ConexionMySQL conexion;

    public User_controller() {
        this.conexion = new ConexionMySQL();
    }

    public Map<String, Object> select() {
        // Inicializamos el mapa de resultados. Este mapa almacenará los nombres de las columnas, el número de columnas y los datos de la tabla.
        Map<String, Object> result = new HashMap<>();
        

        // Intentamos establecer una conexión con la base de datos
        try (Connection conn = conexion.conectarMySQL()) {
            // Verificamos si la conexión fue exitosa
            if (conn != null) {
                // Preparamos la consulta SQL para seleccionar datos de la tabla 'usuario'
                String selectSQL = "SELECT id, name , ide   FROM  users WHERE id in (SELECT  id_user FROM reservation)";

                // Intentamos preparar y ejecutar la consulta SQL
                try (PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
                    // Ejecutamos la consulta y obtenemos los resultados en un ResultSet
                    ResultSet rs = pstmt.executeQuery();

                    // Obtenemos los metadatos del ResultSet. Los metadatos contienen información sobre la estructura de los resultados, como el número de columnas y los nombres de las columnas.
                    ResultSetMetaData rsmd = rs.getMetaData();

                    // Obtenemos el número de columnas de los metadatos
                    int numColumns = rsmd.getColumnCount();

                    // Creamos una lista para almacenar los nombres de las columnas
                    List<String> columnNames = new ArrayList<>();
                    for (int i = 1; i <= numColumns; i++) {
                        // Obtenemos el nombre de cada columna de los metadatos y lo agregamos a la lista
                        columnNames.add(rsmd.getColumnName(i));
                    }

                    // Creamos una lista para almacenar los datos de la tabla
                    List<List<Object>> tableData = new ArrayList<>();
                    while (rs.next()) {
                        // Creamos una lista para almacenar los datos de la fila actual
                        List<Object> rowData = new ArrayList<>();
                        for (int i = 1; i <= numColumns; i++) {
                            // Obtenemos el dato de cada columna de la fila actual y lo agregamos a la lista
                            rowData.add(rs.getObject(i));
                        }
                        // Agregamos la lista de datos de la fila a la lista de datos de la tabla
                        tableData.add(rowData);
                    }

                    // Agregamos el número de columnas, los nombres de las columnas y los datos de la tabla al mapa de resultados
                    result.put("numColumns", numColumns);
                    result.put("columnNames", columnNames);
                    result.put("tableData", tableData);
                }
            } else {
                // Si no se pudo establecer la conexión con la base de datos, imprimimos un mensaje de error
                System.out.println("No se pudo establecer la conexión con la base de datos");
            }
        } catch (SQLException e) {
            // Si ocurre un error al realizar la selección en la base de datos, imprimimos un mensaje de error y la traza de la pila del error
            System.out.println("Ocurrió un error al realizar la selección en la base de datos");
            e.printStackTrace();
        }

        // Imprimimos el mapa de resultados para depuración
        System.out.println(result);

        // Retornamos el mapa de resultados
        return result;
    }

}