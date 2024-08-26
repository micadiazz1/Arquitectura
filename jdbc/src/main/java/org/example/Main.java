package org.example;

import java.sql.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {


            // URL de conexión a la base de datos
            String url = "jdbc:derby:memory:myDB;create=true"; // Cambia 'myDB' al nombre que prefieras

            // Consulta SQL para crear la tabla
            String createTableSQL = "CREATE TABLE Personas (" +
                    "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
                    "nombre VARCHAR(255), " +
                    "apellido VARCHAR(255), " +
                    "email VARCHAR(255))";

            // Conexión y ejecución de la consulta
            try (Connection conn = DriverManager.getConnection(url);
                 Statement stmt = conn.createStatement()) {

                // Ejecutar la consulta para crear la tabla
                stmt.execute(createTableSQL);
                // Agrego dos personas

                agregarPersona(conn,"LULOX","OROQUIETA","asdfg@GMAIL.COM");
                agregarPersona(conn, "María", "González", "maria.gonzalez@example.com");
                printPersons(conn);
                deletePersona(conn,2);
                //updatePerson(conn,2,"Tomas","Oroquieta","sjdksjd@gmail.com");
                printPersons(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }


    }
    private static void agregarPersona(Connection conn, String nombre, String apellido, String email) throws SQLException{
        String insert = "INSERT INTO Personas (nombre, apellido, email) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(insert)) {
            // Asignar valores a los parámetros
            pstmt.setString(1, nombre);
            pstmt.setString(2, apellido);
            pstmt.setString(3, email);

            // Ejecutar la consulta de inserción
            pstmt.executeUpdate();
            System.out.println("Persona agregada exitosamente: " + nombre + " " + apellido);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void deletePersona(Connection conexionDB,int id) throws SQLException{
        String delete ="DELETE from Personas WHERE id=?";
        PreparedStatement pstmt= conexionDB.prepareStatement(delete);
        pstmt.setInt(1,id);
        // Ejecutar la consulta de eliminación
        int rowsAffected = pstmt.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Persona con ID " + id + " eliminada exitosamente.");
        } else {
            System.out.println("No se encontró una persona con el ID: " + id);
        }

    }
    private static void updatePerson(Connection conexionDB,int id, String nuevoNombre, String nuevoApellido,String nuevoEmail)throws SQLException{
        String update = "UPDATE Personas SET nombre = ?, apellido = ?, email = ? WHERE id = ?";
        PreparedStatement pstmt = conexionDB.prepareStatement(update);
        // Asignar valores a los parámetros
        pstmt.setString(1, nuevoNombre);
        pstmt.setString(2, nuevoApellido);
        pstmt.setString(3, nuevoEmail);
        pstmt.setInt(4, id);

        // Ejecutar la consulta de actualización
        int rowsAffected = pstmt.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Persona actualizada exitosamente: " + nuevoNombre + " " + nuevoApellido);
        } else {
            System.out.println("No se encontró una persona con el ID: " + id);
        }

    }
    private static void printPersons(Connection conexionDB) throws SQLException {
        String query = "SELECT * FROM Personas";
        Statement stmt = conexionDB.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            int id = rs.getInt("ID");
            String nombre = rs.getString("NOMBRE");
            String apellido = rs.getString("APELLIDO");
            String email = rs.getString("EMAIL");
            System.out.println("ID: " + id + ", Nombre: " + nombre + ", Apellido: " + apellido + ", Email: " + email);
        }

    }
}