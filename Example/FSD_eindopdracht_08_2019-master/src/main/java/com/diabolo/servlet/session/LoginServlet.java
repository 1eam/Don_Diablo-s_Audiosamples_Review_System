package com.diabolo.servlet.session;

import com.diabolo.db.ConnectionManager;
import com.diabolo.util.PasswordUtil;
import com.diabolo.util.RegexUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.*;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final int MAX_AGE_SECONDS = 60;
    private Logger Logger = LogManager.getLogger(getClass().getName());
    private boolean userActive = false;
    private boolean twoFactorActive = false;
    private boolean tempPassword = false;
    private boolean emailNotValid = false;
    private PrintWriter writer;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user = request.getParameter("user");
        String pwd = request.getParameter("pwd");

        writer = response.getWriter();

        if (isIndirectRequest(request)) {
            pwd = request.getParameter("newpassword");
        }

        try (Connection con = ConnectionManager.getConnection("login", "login_read")) {
            if (con == null) {
                //Todo: redirect to no database connection error page
                writer.println("<font color=red>No database connection!</font>");
                return;
            }

            ResultSet rs = retrieveUserData(con, user);
            if (rs == null) {
                //Todo: redirect to empty ResultSet error page
                writer.println("<font color=red>Empty ResultSet!</font>");
                return;
            }

            if (!userFound(request, rs)) {
                return;
            }

            if (!userActive) {
                //Todo: redirect to user disabled in database error page
                writer.println("<font color=red>User has been disabled in database!</font>");
                logLoginInfo(rs.getInt(1), request, false);
                return;
            }

            if (!PasswordUtil.passwordIsVerified(rs.getString(2), pwd)) {
                //Todo: redirect to password or username not in database error page
                writer.println("<font color=red>Either password or username is not in database!</font>");
                logLoginInfo(rs.getInt(1), request, false);
                return;
            }

            HttpSession session = createNewSession(request);
            if (twoFactorActive) {
                session.setAttribute("tempid", rs.getInt(1));
                session.setAttribute("tempuser", user);
                //setting session to expire in 60 seconds
                session.setMaxInactiveInterval(60);
                response.sendRedirect("2falogin.jsp");
            } else if (tempPassword) {
                response.sendRedirect("passwordchange.jsp");
            } else if (emailNotValid) {
                response.sendRedirect("emailvalidation.jsp");
            } else {
                fillSession(session, rs, user);
                createCookie(response, user);
                response.sendRedirect("LoginSuccess.jsp");
            }
            logLoginInfo(rs.getInt(1), request, true);
            rs.close();
        } catch (SQLException ex) {
            Logger.error(getClass().getName() + " SQL Error Message Logged !!!", ex.getMessage(), ex);
        }
    }

    private void createCookie(HttpServletResponse response, String user) {
        Cookie userCookie = new Cookie("user", user);
        userCookie.setMaxAge(MAX_AGE_SECONDS);
        response.addCookie(userCookie);
    }

    private void fillSession(HttpSession session, ResultSet rs, String user) throws SQLException {
        session.setAttribute("userid", rs.getInt(1));
        session.setAttribute("username", user);
        session.setAttribute("firstname", rs.getString(6));
        session.setAttribute("lastname", rs.getString(7));
        session.setAttribute("email", rs.getString(8));
        session.setAttribute("groupid", rs.getInt(10));
        session.setAttribute("groupname", rs.getString(11));
        session.setMaxInactiveInterval(MAX_AGE_SECONDS);
    }

    private boolean isIndirectRequest(HttpServletRequest request) {
        if (request.getAttribute("javax.servlet.forward.servlet_path") == null) {
            return false;
        }
        if (!request.getAttribute("javax.servlet.forward.servlet_path").toString().equals("/PasswordChangeServlet")) {
            return false;
        }
        return request.getAttribute("requestAttr").toString().equals("ChangeThePassword");
    }

    private boolean userFound(HttpServletRequest request, ResultSet rs) throws SQLException {
        int rowCount = 0;
        if (rs.last()) {
            rowCount = rs.getRow();
            rs.beforeFirst();
        } else {
            //Todo: redirect to username or password not in database error page
            writer.println("<font color=red>Either username or password is not in database!</font>");
            logLoginInfo(null, request, false);
            return false;
        }
        if (rowCount != 1) {
            //Todo: redirect to duplicate entry in database error page
            writer.println("<font color=red>Duplicate entry in database!</font>");
            logLoginInfo(null, request, false);
            return false;
        }

        if (rs.next()) {
            userActive = rs.getBoolean(5);
            twoFactorActive = rs.getBoolean(4);
            tempPassword = rs.getBoolean(3);
            emailNotValid = rs.getBoolean(9);
            return true;
        }
        return false;
    }

    private HttpSession createNewSession(HttpServletRequest request) {
        request.getSession().invalidate(); //invalidate current session
        return request.getSession(true); //create new session ID to prevent hijacking
    }

    private ResultSet retrieveUserData(Connection con, String userName) throws SQLException {
        String query = "SELECT u.id, u.password, u.temp_password, " +
                "IF(u.secret IS NULL OR u.secret = '' OR u.salt IS NULL OR u.salt = '', 0, 1) AS secretFilled, " +
                "u.active, i.firstname, i.lastname, i.email, i.email_not_valid, g.id, g.name FROM user AS u " +
                "JOIN user_info AS i ON i.user_id = u.id " +
                "JOIN `group` AS g ON u.group_id = g.id " +
                "WHERE u.username = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1, userName);
        ResultSet rs = pst.executeQuery();
        pst.close();
        return rs;
    }

    private void logLoginInfo(Integer userId, HttpServletRequest request, boolean loginSuccessful) {
        String query = "INSERT INTO login_info (user_id, last_login, ip_address, header_info, login_successful) " +
                "VALUES (?, NOW(), ?, ?, ?)";
        try (Connection con = ConnectionManager.getConnection("login", "login_info_rw")) {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setObject(1, userId);
            pst.setString(2, getIpAddress(request));
            pst.setString(3, getHeaderJSON(request).toString());
            pst.setBoolean(4, loginSuccessful);
            pst.executeUpdate();
            pst.close();
        } catch (SQLException ex) {
            Logger.error(getClass().getName() + " SQL Error Message Logged !!!", ex.getMessage(), ex);
        }
    }

    private JSONObject getHeaderJSON(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            map.put(headerName, headerValue);
        }
        map.put("request-uri", request.getRequestURI());
        JSONObject obj = new JSONObject(map);

        Map<String, String> map2 = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            String parameterValue = request.getParameter(parameterName);
            // This section is to filter out valid password coming from the front-end
            // No passwords should be stored in database
            if (parameterName.equals("pwd")) {
                if (!RegexUtil.hasValidPattern(parameterValue, RegexUtil.PWD_PATTERN)) {
                    parameterValue = ""; // Set a valid password to blank
                }
            }
            map2.put(parameterName, parameterValue);
        }
        map2.put("method", request.getMethod());
        JSONArray array = new JSONArray();
        array.put(map2);
        obj.put("parameters", array);
        return obj;
    }

    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
