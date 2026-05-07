package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnexioBD {
    private static final String URL = "jdbc:mysql://localhost:3306/tpv_botiga";

    private static final String USER = "root";

    private static final String PASSWORD = "";

    public static Connection connectar() {

        Connection conn = null;

        try {

            conn = DriverManager.getConnection(URL, USER, PASSWORD
            );

            System.out.println("Connexió correcta!");

        } catch (SQLException e) {

            System.out.println("Error de connexió");
            e.printStackTrace();
        }

        return conn;
    }
}
