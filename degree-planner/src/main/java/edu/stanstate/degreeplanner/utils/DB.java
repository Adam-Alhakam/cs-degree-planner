package edu.stanstate.degreeplanner.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DB {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/degree_planner?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "appuser";
    private static final String PASS = "app123";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found.", e);
        }
    }

    private DB() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}