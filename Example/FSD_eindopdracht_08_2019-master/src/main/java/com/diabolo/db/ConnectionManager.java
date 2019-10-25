package com.diabolo.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
    private static final Logger LOGGER = LogManager.getLogger(ConnectionManager.class.getName());

    public static Connection getConnection(String databaseName, String roleName) throws SQLException {
        Properties props = DatabaseProperties.getConnectionData();

        String url = props.getProperty("db.url");
        String driver = props.getProperty("db.driver");

        String dbName = props.getProperty("db."+databaseName+".name");
        String user = props.getProperty("db."+databaseName+"."+roleName+".user");
        String passwd = props.getProperty("db."+databaseName+"."+roleName+".passwd");

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            LOGGER.error(ConnectionManager.class.getName() + " Error Message Logged !!!", ex.getMessage(), ex);
        }
        return DriverManager.getConnection(url+dbName,user,passwd);
    }
}
