package com.diabolo.servlet.register;

import com.diabolo.util.EmailUtil;
import com.diabolo.util.RegexUtil;
import com.diabolo.util.UserUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet("/CreateNewUser")
public class CreateUserServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String[]> parameterMap = request.getParameterMap();
        PrintWriter writer = response.getWriter();

        if (!isValidFormInput(parameterMap)) {
            //Todo: redirect to invalid form input error page
            writer.println("<font color=red>Not all fields match required input patterns.</font>");
            return;
        }

        if (EmailUtil.isEmailInDatabase(parameterMap.get("email")[0])) {
            //Todo: redirect to email address in database error page
            writer.println("<font color=red>Email address already in the database.</font>");
            return;
        }

        if (UserUtil.isUserInDatabase(parameterMap.get("user")[0])) {
            //Todo: redirect to username in database error page
            writer.println("<font color=red>Username already in the database.</font>");
            return;
        }

        String validation = EmailUtil.createValidationString(100);
        if (!UserUtil.insertUserInDatabase(parameterMap, "producer", validation)) {
            //Todo: redirect to insert into database error page
            writer.println("<font color=red>Error while inserting into database.</font>");
            return;
        }
        String fullName = parameterMap.get("firstname")[0] + " " + parameterMap.get("lastname")[0];
        EmailUtil.sendNewUserEmail(parameterMap.get("email")[0], fullName, validation);

        response.sendRedirect("emailvalidation.jsp");
    }

    private boolean isValidFormInput(Map<String, String[]> parameterMap) {
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            if (!RegexUtil.hasValidPattern(entry.getValue()[0], RegexUtil.getPatternByInputName(entry.getKey()))) {
                return false;
            }
        }
        return true;
    }
}
