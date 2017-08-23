package io.robusta.auth.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLDatabaseConnection {

    private Connection connection;

    public MySQLDatabaseConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/authentication", "root", "");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Echec du chargement du driver", e);
        } catch (SQLException e) {
            throw new RuntimeException("Impossible d'établir une connection avec le SGBD", e);
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
