package com.diabolo.servlet.password;

import com.diabolo.db.ConnectionManager;
import com.diabolo.util.PasswordUtil;
import com.diabolo.util.RegexUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
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

@WebServlet("/PasswordChangeServlet")
public class ChangePasswordServlet extends HttpServlet {
    private Logger Logger = LogManager.getLogger(getClass().getName());
    private boolean userActive = false;
    private boolean emailNotValid = false;
    private PrintWriter writer;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user = request.getParameter("user");
        String passOld = request.getParameter("oldpassword");
        String passNew = request.getParameter("newpassword");
        String passNewConfirm = request.getParameter("newpassword2");
        writer = response.getWriter();

        if (!passNew.equals(passNewConfirm)) {
            //Todo: redirect to passwords not match error
            writer.println("<font color=red>Passwords to not match!</font>");
            return;
        }
        if (passNew.equals(passOld)) {
            //Todo: redirect to new and old password match error
            writer.println("<font color=red>New and old passwords cannot match!</font>");
            return;
        }

        if (!isValidFormInput(user, passOld, passNew, passNewConfirm)) {
            //Todo: redirect to not valid input error page
            writer.println("<font color=red>Not all input is valid</font>");
            return;
        }

        try (Connection con = ConnectionManager.getConnection("login", "login_read")) {
            if (con == null) {
                //Todo: redirect to no database connection error page
                writer.println("<font color=red>No database connection!</font>");
                return;
            }

            ResultSet rs = retrieveLoginData(con, user);
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
                //logLoginInfo(rs.getInt(1), request, false);
                return;
            }

            if (emailNotValid) {
                //Todo: redirect to validate email page
                writer.println("<font color=red>Email not yet validated</font>");
                return;
            }

            if (!PasswordUtil.passwordIsVerified(rs.getString(2), passOld)) {
                //Todo: redirect to password or username not in database error page
                writer.println("<font color=red>Either password or username is not in database!</font>");
                //logLoginInfo(rs.getInt(1), request, false);
                return;
            }

            if (updatePasswordInDatabase(rs.getInt(1), PasswordUtil.hashPassword(passNew)) == 0) {
                //Todo: redirect to password update in database error page
                writer.println("<font color=red>Something went wrong while updating database!</font>");
            }

            rs.close();

            request.setAttribute("requestAttr", "ChangeThePassword");
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/LoginServlet");
            dispatcher.forward(request, response);
        } catch (SQLException ex) {
            Logger.error(getClass().getName() + " Error Message Logged !!!", ex.getMessage(), ex);
        }

        writer.println("<font color=green>Still going strong</font>");
    }

    private int updatePasswordInDatabase(int userId, String passwordHash) {
        int updateSuccess = 0;
        try (Connection con = ConnectionManager.getConnection("login", "login_rw")) {
            String query = "UPDATE user SET password = ?, temp_password = 0 WHERE id = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, passwordHash);
            pst.setInt(2, userId);
            updateSuccess = pst.executeUpdate();
            pst.close();
        } catch (SQLException ex) {
            Logger.error(getClass().getName() + " Error Message Logged !!!", ex.getMessage(), ex);
        }
        return updateSuccess;
    }

    private boolean userFound(ResultSet rs) throws SQLException {
        int rowCount = 0;
        if (rs.last()) {
            rowCount = rs.getRow();
            rs.beforeFirst();
        } else {
            //Todo: redirect to username or password not in database error page
            writer.println("<font color=red>Either username or password is not in database!</font>");
            //logLoginInfo(null, request, false);
            return false;
        }
        if (rowCount != 1) {
            //Todo: redirect to duplicate entry in database error page
            writer.println("<font color=red>Duplicate entry in database!</font>");
            //logLoginInfo(null, request, false);
            return false;
        }

        if (rs.next()) {
            userActive = rs.getBoolean(3);
            emailNotValid = rs.getBoolean(4);
            return true;
        }
        return false;
    }

    private ResultSet retrieveLoginData(Connection con, String userName) throws SQLException {
        String query = "SELECT u.id, u.password, u.active, i.email_not_valid FROM user AS u " +
                "JOIN user_info AS i ON i.user_id = u.id WHERE u.username = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1, userName);
        ResultSet rs = pst.executeQuery();
        pst.close();
        return rs;
    }

    private boolean isValidFormInput(String userName, String oldPwd, String newPwd, String confirmNewPwd) {
        return RegexUtil.hasValidPattern(userName, RegexUtil.USER_PATTERN) &&
                RegexUtil.hasValidPattern(oldPwd, RegexUtil.OLD_PASS_PATTERN) &&
                RegexUtil.hasValidPattern(newPwd, RegexUtil.NEW_PASS_PATTERN) &&
                RegexUtil.hasValidPattern(confirmNewPwd, RegexUtil.NEW_PASS_PATTERN);
    }
}
