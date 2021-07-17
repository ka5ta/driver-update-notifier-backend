package com.ka5ta.drivers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private final String url = "jdbc:postgresql://localhost:5432/drivers";
    private final String user = "postgres";
    private final String password = "admin";

    /**
     ## Connect to PostgreSQL database ##
        * @return a Connection object
     */

    public Connection connect()  {
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return conn;
    }


}
