package com.diabolo.api;

import com.diabolo.api.annotation.PathMapper;
import com.diabolo.api.annotation.PathVariable;
import com.diabolo.util.EmailUtil;
import com.diabolo.util.RegexUtil;
import com.diabolo.util.UserUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Scrutinize, obliterate, amend & develop (SOAD) Controller.
 * SOAD is my self made implementation what CRUD is in Spring Boot.
 */
public class AjaxResponseController {
    private HttpServletResponse response;
    private HttpServletRequest request;
    private PrintWriter writer;

    public AjaxResponseController(HttpServletResponse response, HttpServletRequest request) throws IOException {
        this.response = response;
        this.request = request;
        writer = response.getWriter();
    }

    private void setPlainContent() {
        response.setContentType("text/plain");
    }

    private void setHTMLContent() {
        response.setContentType("text/html;charset=UTF-8");
    }

    private void setJSONContent() {
        response.setContentType("application/json");
    }

    @PathMapper("soad/Username/{userName}")
    private void usernameRequest(@PathVariable String userName) {
        setPlainContent();
        if (!RegexUtil.hasValidPattern(userName, RegexUtil.USER_PATTERN)) {
            //Todo: redirect to no valid email format error page
            writer.println("<font color=red>Not a valid email format!</font>");
            return;
        }
        writer.print(UserUtil.isUserInDatabase(userName));
    }

    @PathMapper("soad/Email/{email}")
    private void emailRequest(@PathVariable String email) {
        setPlainContent();
        if (!RegexUtil.hasValidPattern(email, RegexUtil.EMAIL_PATTERN)) {
            //Todo: redirect to no valid email format error page
            writer.println("<font color=red>Not a valid email format!</font>");
            return;
        }
        writer.print(EmailUtil.isEmailInDatabase(email));
    }

    @PathMapper("soad")
    private void soadRequest() {
        try {
            response.sendRedirect("../soad.jsp");
        } catch (IOException ex) {}
    }

    @PathMapper("soad/soad/soad")
    private void soadSoadSoadRequest() {
        try {
            response.sendRedirect("../../../soadsoadsoad.jsp");
        } catch (IOException ex) {}
    }
}
