package com.diabolo.servlet.password;

import com.diabolo.db.ConnectionManager;
import com.diabolo.util.EmailUtil;
import com.diabolo.util.PasswordUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/PasswordResetServlet")
public class ResetPasswordServlet extends HttpServlet {
    private Logger Logger = LogManager.getLogger(getClass().getName());
    private boolean userActive = false;
    private boolean emailNotValid = false;
    private PrintWriter writer;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user = request.getParameter("user");
        String email = request.getParameter("email");
        writer = response.getWriter();

        if (email.contains("fullslack.dev") || email.contains("dondiabolo.com")) {
            //Todo: redirect to invalid domain in email address error page
            writer.println("<font color=red>Invalid domain in email address!</font>");
            return;
        }

        try (Connection con = ConnectionManager.getConnection("login", "login_read")) {
            if (con == null) {
                //Todo: redirect to no database connection error page
                writer.println("<font color=red>No database connection!</font>");
                return;
            }

            ResultSet rs = retrieveUserData(con, user, email);
            if (rs == null) {
                //Todo: redirect to empty ResultSet error page
                writer.println("<font color=red>Empty ResultSet!</font>");
                return;
            }

            if (!userFound(rs)) {
                return;
            }

            if (!userActive) {
                //Todo: redirect to user disabled in database error page
                writer.println("<font color=red>User has been disabled in database!</font>");
                return;
            }

            if (emailNotValid) {
                //Todo: redirect to email validation error page
                response.sendRedirect("emailvalidation.jsp");
                return;
            }

            //Todo (redirect to password change) or (redirect to confirm page, check email)
            String tempPassword = PasswordUtil.generatePassword(20);
            String passwordHash = PasswordUtil.hashPassword(tempPassword);
            int userId = rs.getInt(1);
            String name = rs.getString(4) + " " + rs.getString(5);
            if (updatePasswordInDatabase(userId, passwordHash) > 0) {
                EmailUtil.sendPasswordEmail(email, name, tempPassword);
                response.sendRedirect("passwordchange.jsp");
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.error(getClass().getName() + " SQL Error Message Logged !!!", ex.getMessage(), ex);
        }
    }

    private ResultSet retrieveUserData(Connection con, String user, String email) throws SQLException {
        String query = "SELECT u.id, u.temp_password, u.active, i.firstname, i.lastname, i.email_not_valid " +
                "FROM user AS u " +
                "JOIN user_info AS i ON i.user_id = u.id WHERE u.username = ? AND i.email = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1, user);
        pst.setString(2, email);
        ResultSet rs = pst.executeQuery();
        pst.close();
        return rs;
    }

    private boolean userFound(ResultSet rs) throws SQLException {
        int rowCount = 0;
        if (rs.last()) {
            rowCount = rs.getRow();
            rs.beforeFirst();
        } else {
            //Todo: redirect to username or email not in database error page
            writer.println("<font color=red>Either username or email is not in database!</font>");
            return false;
        }
        if (rowCount != 1) {
            //Todo: redirect to duplicate entry in database error page
            writer.println("<font color=red>Duplicate entry in database!</font>");
            return false;
        }

        if (rs.next()) {
            userActive = rs.getBoolean(3);
            emailNotValid = rs.getBoolean(6);
            return true;
        }
        return false;
    }

    private int updatePasswordInDatabase(int userId, String passwordHash) {
        int updateSuccess = 0;
        try (Connection con = ConnectionManager.getConnection("login", "login_rw")) {
            String query = "UPDATE user SET password = ?, temp_password = 1 WHERE id = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, passwordHash);
            pst.setInt(2, userId);
            updateSuccess = pst.executeUpdate();
            pst.close();
        } catch (SQLException ex) {
            Logger.error(getClass().getName() + " SQL Error Message Logged !!!", ex.getMessage(), ex);
        }
        return updateSuccess;
    }
}
