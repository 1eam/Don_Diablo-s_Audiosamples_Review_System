package com.diabolo.servlet.session;

import com.diabolo.db.ConnectionManager;
import com.diabolo.util.TOTPSecretUtil;
import com.diabolo.util.TimeBasedOneTimePasswordUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/TwoFactorServlet")
public class TwoFactorServlet extends HttpServlet {
    private static final int MAX_AGE_SECONDS = 60;
    private Logger Logger = LogManager.getLogger(getClass().getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();
        try (Connection con = ConnectionManager.getConnection("login","login_read")) {
            int code = Integer.parseInt(request.getParameter("code"));
            if (con == null) {
                //Todo: redirect to no database connection error page
                writer.println("<font color=red>No database connection!</font>");
                return;
            }

            HttpSession session = request.getSession(false);
            if (session == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            if (session.getAttribute("tempuser") == null) {
                if (request.isRequestedSessionIdValid()) {
                    session.invalidate();
                }
                request.getSession(true);
                response.sendRedirect("login.jsp");
                return;
            }

            int userId = (int) session.getAttribute("tempid");
            String userName = (String) session.getAttribute("tempuser");
            ResultSet rs = retrieveTwoFactorData(con, userId, userName);
            if (rs == null) {
                //Todo: redirect to empty ResultSet error page
                writer.println("<font color=red>Empty ResultSet!</font>");
                return;
            }

            if (!rs.next()) {
                return;
            }

            boolean isValidCode = hasValidCode(rs, code);

            if (!isValidCode) {
                //Todo: redirect to code is not valid error page
                writer.println("<font color=red>Code is not valid!</font>");
                return;
            }

            fillSession(session, rs, userId, userName);
            createCookie(response, userName);

            rs.close();
            response.sendRedirect("LoginSuccess.jsp");
        } catch (SQLException ex) {
            Logger.error(getClass().getName() + " SQL Error Message Logged !!!", ex.getMessage(), ex);
        } catch (GeneralSecurityException ex) {
            Logger.error(getClass().getName() + " TOTP Error Message Logged !!!", ex.getMessage(), ex);
        } catch (NumberFormatException ex) {
            //Todo: redirect to not a valid 6 digit code error page
            writer.println("<font color=red>Not a valid 6 digit number code!</font>");
        }
    }

    private void createCookie(HttpServletResponse response, String userName) {
        Cookie userCookie = new Cookie("user", userName);
        userCookie.setMaxAge(MAX_AGE_SECONDS);
        response.addCookie(userCookie);
    }

    private void fillSession(HttpSession session, ResultSet rs, int userId, String userName) throws SQLException {
        session.removeAttribute("tempid");
        session.removeAttribute("tempuser");
        session.setAttribute("userid", userId);
        session.setAttribute("username", userName);
        session.setAttribute("firstname", rs.getString(3));
        session.setAttribute("lastname", rs.getString(4));
        session.setAttribute("email", rs.getString(5));
        session.setAttribute("groupid", rs.getInt(6));
        session.setAttribute("groupname", rs.getString(7));
        session.setMaxInactiveInterval(MAX_AGE_SECONDS);
    }

    private boolean hasValidCode(ResultSet rs, int code) throws SQLException, GeneralSecurityException {
        TOTPSecretUtil secretUtil = new TOTPSecretUtil();
        String secretKeySpec = rs.getString(1);
        String encryptedBase32Secret = rs.getString(2);
        String base32Secret = secretUtil.decrypt(encryptedBase32Secret, secretKeySpec);
        return TimeBasedOneTimePasswordUtil.validateCurrentNumber(base32Secret, code, 2000);
    }

    private ResultSet retrieveTwoFactorData(Connection con, int userId, String userName) throws SQLException {
        String query = "SELECT u.salt, u.secret, i.firstname, i.lastname, i.email, g.id, g.name FROM user AS u " +
                "JOIN user_info AS i ON i.user_id = u.id " +
                "JOIN `group` AS g ON u.group_id = g.id " +
                "WHERE u.id = ? AND u.username = ?";PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1, userId);
        pst.setString(2, userName);
        ResultSet rs = pst.executeQuery();
        pst.close();
        return rs;
    }
}
