package com.diabolo.util;

import com.diabolo.db.ConnectionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.Map;

public class UserUtil {
    private static final Logger LOGGER = LogManager.getLogger(UserUtil.class.getName());

    public static boolean isUserInDatabase(String userName) {
        boolean returnValue = false;
        String query = "SELECT id FROM user WHERE username = ?";
        try (Connection con = ConnectionManager.getConnection("login", "login_read")) {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, userName);
            ResultSet rs = pst.executeQuery();
            if (rs.last()) {
                returnValue = true;
            }
            rs.close();
            pst.close();
        } catch (SQLException ex) {
            LOGGER.error(UserUtil.class.getName() + " SQL Error Message Logged !!!", ex.getMessage(), ex);
        }
        return returnValue;
    }

    public static boolean insertUserInDatabase(Map<String, String[]> paramMap, String groupName, String validation) {
        boolean insertSuccess = false;
        try (Connection con = ConnectionManager.getConnection("login", "login_new")) {
            String query = "{CALL new_user (?, ?, ?, ?, ?, ?, ?, ?, ?)}";
            CallableStatement cst = con.prepareCall(query);
            String hashedPassword = PasswordUtil.hashPassword(paramMap.get("newpassword")[0]);
            cst.setString(1, groupName);
            cst.setString(2, paramMap.get("user")[0]);
            cst.setString(3, hashedPassword);
            cst.setString(4, paramMap.get("firstname")[0]);
            cst.setString(5, paramMap.get("lastname")[0]);
            cst.setString(6, paramMap.get("country")[0]);
            cst.setString(7, paramMap.get("email")[0]);
            cst.setString(8, validation);
            cst.registerOutParameter(9, Types.BOOLEAN);
            cst.execute();
            insertSuccess = cst.getBoolean(9);
            cst.close();
        } catch (SQLException ex) {
            LOGGER.error(UserUtil.class.getName() + " SQL Error Message Logged !!!", ex.getMessage(), ex);
        }
        return insertSuccess;
    }
}
