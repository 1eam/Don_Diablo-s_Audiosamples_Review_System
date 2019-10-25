package com.diabolo.servlet.demo;

import com.diabolo.security.SessionValidator;
import com.diabolo.util.DemoUtil;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/DemoAudioStream")
public class DemoStreamServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletOutputStream servletOutputStream = response.getOutputStream();
        PrintWriter writer = new PrintWriter(servletOutputStream);

        HttpSession session;
        SessionValidator validator = new SessionValidator(request, response);
        if (!(validator.isValidSession()) || !(validator.isValidCookies())) {
            writer.println("<font color=red>Session expired.</font>");
            return;
        }
        session = validator.getSession();

        String fileName = request.getParameter("file");
        String downloadName = request.getParameter("title");
        boolean isDownload = request.getParameter("download") != null;
        String storageDir = System.getProperty("java.io.tmpdir") + File.separator + "DonDiablo" + File.separator;
        String filePath = storageDir + fileName;
        response.setContentType("audio/mpeg");
        if (isDownload) {
            response.setHeader("Content-Disposition", "attachment; filename=\""+ downloadName +".mp3\"");
        }

        DemoUtil.streamDemo(servletOutputStream, filePath);
    }
}
