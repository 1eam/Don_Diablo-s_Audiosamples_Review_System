package com.diabolo.servlet.email;

import com.diabolo.util.EmailUtil;
import com.diabolo.util.RegexUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/ValidateEmail")
public class ValidateEmailServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String validationCode = request.getParameter("code");
        PrintWriter writer = response.getWriter();

        if (!RegexUtil.hasValidPattern(validationCode, RegexUtil.VALIDATION_PATTERN)) {
            //Todo: redirect to no valid validation code error page
            writer.println("<font color=red>Not a valid validation code!</font>");
            return;
        }

        if (!EmailUtil.isValidationStringInDatabase(validationCode)) {
            //Todo: redirect to validation code not in database error page
            writer.println("<font color=red>Validation code not in database!</font>");
            return;
        }

        EmailUtil.disableValidationInDatabase(validationCode);

        response.sendRedirect("login.jsp");
    }
}
