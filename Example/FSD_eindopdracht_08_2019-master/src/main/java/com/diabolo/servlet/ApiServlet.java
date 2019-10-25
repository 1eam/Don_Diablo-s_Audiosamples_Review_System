package com.diabolo.servlet;

import com.diabolo.api.PathHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/*")
public class ApiServlet extends HttpServlet {
    private Logger Logger = LogManager.getLogger(getClass().getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo().substring(1); // strip / at beginning
        try {
            PathHandler.handleRequest(path, response, request);
        } catch (Exception ex) {
            Logger.error(getClass().getName() + " Error Message Logged !!!", ex.getMessage(), ex);
        }
    }
}
