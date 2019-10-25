package com.diabolo.security;

import com.diabolo.db.ConnectionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ActiveUserValidator {
    private Logger Logger = LogManager.getLogger(getClass().getName());

    private HttpSession session;
    private HttpServletRequest request;
    private HttpServletResponse response;

    public ActiveUserValidator(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        session = request.getSession(false);
    }

    public boolean isUserActive() {
        boolean userActive = false;
        try (Connection con = ConnectionManager.getConnection("login","login_read")) {
            String query = "SELECT active FROM user WHERE id = ? AND username = ?";
            if (con == null) {
                invalidateSession(HttpServletResponse.SC_SERVICE_UNAVAILABLE); //HTTP 503
                return userActive;
            }

            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, (int) session.getAttribute("userid"));
            pst.setString(2, (String) session.getAttribute("username"));
            ResultSet rs = pst.executeQuery();

            if (rs == null) {
                invalidateSession(HttpServletResponse.SC_BAD_GATEWAY); //HTTP 502
                return userActive;
            }

            userActive = userFound(rs);
            rs.close();
            pst.close();
        } catch (SQLException ex) {
            Logger.error(getClass().getName() + " Error Message Logged !!!", ex.getMessage(), ex);
        }
        return userActive;
    }

    private boolean userFound(ResultSet rs) throws SQLException {
        int rowCount = 0;
        if (rs.last()) {
            rowCount = rs.getRow();
            rs.beforeFirst();
        } else {
            invalidateSession(HttpServletResponse.SC_CONFLICT); //HTTP 409
        }
        if (rowCount != 1) {
            invalidateSession(HttpServletResponse.SC_UNAUTHORIZED); //HTTP 401
        }

        if (rs.next()) {
            return true;
        }
        return false;
    }

    private void invalidateSession(int httpStatus) {
        if (request.isRequestedSessionIdValid()) {
            session.invalidate();
        }
        request.getSession(true);
        response.setStatus(httpStatus);
    }
}
