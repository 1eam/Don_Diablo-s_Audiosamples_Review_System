package com.diabolo.servlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySpecialListener implements ServletContextListener {
    private Logger Logger = LogManager.getLogger(getClass().getName());

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            java.sql.Driver mySqlDriver = DriverManager.getDriver("jdbc:mariadb://localhost:13306/");
            DriverManager.deregisterDriver(mySqlDriver);
        } catch (SQLException ex) {
            Logger.error(getClass().getName() + " SQL Error Message Logged !!!", ex.getMessage(), ex);
        }
    }
}
