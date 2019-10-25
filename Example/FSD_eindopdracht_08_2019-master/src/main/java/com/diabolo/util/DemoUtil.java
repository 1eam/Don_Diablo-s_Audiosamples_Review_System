package com.diabolo.util;

import com.diabolo.db.ConnectionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletOutputStream;
import java.io.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DemoUtil {
    private static final Logger LOGGER = LogManager.getLogger(DemoUtil.class.getName());

    public static boolean createStorageDirectory(String storagePath) {
        try {
            File storageDir = new File(storagePath);
            if (storageDir.exists()) {
                return true;
            } else {
                return storageDir.mkdir();
            }
        } catch (SecurityException ex) {
            LOGGER.error(DemoUtil.class.getName() + " Create Storage Directory Error Message Logged !!!",
                    ex.getMessage(), ex);
        }
        return false;
    }

    public static boolean deleteFile(String filePath) {
        try {
            File file = new File(filePath);
            return file.delete();
        } catch (SecurityException ex) {
            LOGGER.error(DemoUtil.class.getName() + " Create Storage Directory Error Message Logged !!!",
                    ex.getMessage(), ex);
        }
        return false;
    }

    public static boolean moveFile(String fromPath, String toPath) {
        try {
            File file = new File(fromPath);
            return file.renameTo(new File(toPath));
        } catch (SecurityException ex) {
            LOGGER.error(DemoUtil.class.getName() + " Move File Error Message Logged !!!",
                    ex.getMessage(), ex);
        } catch (NullPointerException ex) {
            LOGGER.error(DemoUtil.class.getName() + " Empty Destination Error Message Logged !!!",
                    ex.getMessage(), ex);
        }
        return false;
    }

    public static boolean isValidContentType(String contentType) {
        return contentType.equalsIgnoreCase("video/ogg") ||
                contentType.equalsIgnoreCase("audio/wav") ||
                contentType.equalsIgnoreCase("audio/mpeg");
    }

    public static Map<String, Object> convertDemo(String workDir, String inputFileName, String outputFileName) {
        Jave2Util jave2Util = new Jave2Util(workDir + inputFileName);
        if (jave2Util.encodeAudio(jave2Util.createEncodingAttributes(
                jave2Util.createAudioAttributes("libmp3lame"),"mp3")
                ,workDir + outputFileName)) {
            Map<String, Object> map = new HashMap<>();
            map.put("format", jave2Util.getInputFormat());
            map.put("duration", jave2Util.getInputDuration());
            map.put("bitrate", Integer.toString(jave2Util.getInputBitRate()));
            map.put("origname", inputFileName);
            map.put("outputname", outputFileName);
            return map;
        }
        return null;
    }

    public static String createUniqueFileName(int length) {
        String uniqueFileName = "";
        do {
            uniqueFileName = PasswordUtil.generatePassword(length);
        } while (isFileNameInDatabase(uniqueFileName));
        return uniqueFileName;
    }

    private static boolean isFileNameInDatabase(String fileName) {
        boolean returnValue = false;
        String query = "SELECT id FROM demo WHERE filename = ?";
        try (Connection con = ConnectionManager.getConnection("demo", "demo_read")) {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, fileName);
            ResultSet rs = pst.executeQuery();
            if (rs.last()) {
                returnValue = true;
            }
            rs.close();
            pst.close();
        } catch (SQLException ex) {
            LOGGER.error(DemoUtil.class.getName() + " SQL Error Message Logged !!!", ex.getMessage(), ex);
        }
        return returnValue;
    }

    public static boolean insertDemoIntoDatabase(Map<String, Object> audioMap) {
        boolean returnValue = false;
        String statusIdQuery = "SELECT id FROM action_status WHERE `name` = 'open'";
        String actionIdQuery = "SELECT id FROM action_status WHERE `name` = 'upload'";
        String demoQuery = "INSERT INTO demo (user_id, status_id, title, filename, duration) " +
                "VALUES (?, (" + statusIdQuery + "), ?, ?, ?)";
        String uploadHistoryQuery = "INSERT INTO upload_history (demo_id, orig_filename, orig_format, orig_bitrate) " +
                "VALUES (?, ?, ?, ?);";
        String accessHistoryQuery = "INSERT INTO access_history (user_id, demo_id, action_id) " +
                "VALUES (?, ?, (" + actionIdQuery + "))";
        try (Connection con = ConnectionManager.getConnection("demo", "demo_rw")) {
            int userId = (int)audioMap.get("userid");
            PreparedStatement demoStmt = con.prepareStatement(demoQuery, Statement.RETURN_GENERATED_KEYS);
            demoStmt.setInt(1, userId);
            demoStmt.setString(2, (String)audioMap.get("title"));
            demoStmt.setString(3, (String)audioMap.get("outputname"));
            demoStmt.setLong(4, (long)audioMap.get("duration"));
            returnValue = demoStmt.executeUpdate() > 0;
            ResultSet rs = demoStmt.getGeneratedKeys();
            int demoId = 0;
            if (rs.next()) {
                demoId = rs.getInt(1);
            }

            PreparedStatement uploadHistoryStmt = con.prepareStatement(uploadHistoryQuery);
            uploadHistoryStmt.setInt(1, demoId);
            uploadHistoryStmt.setString(2, (String)audioMap.get("origname"));
            uploadHistoryStmt.setString(3, (String)audioMap.get("format"));
            uploadHistoryStmt.setString(4, (String)audioMap.get("bitrate"));
            returnValue = uploadHistoryStmt.executeUpdate() > 0;

            PreparedStatement accessHistoryStmt = con.prepareStatement(accessHistoryQuery);
            accessHistoryStmt.setInt(1, userId);
            accessHistoryStmt.setInt(2, demoId);
            returnValue = accessHistoryStmt.executeUpdate() > 0;

            return returnValue;
        } catch (SQLException ex) {
            LOGGER.error(DemoUtil.class.getName() + " SQL Error Message Logged !!!", ex.getMessage(), ex);
        }
        return returnValue;
    }

    public static void streamDemo(ServletOutputStream servletOutputStream, String filePath) {
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);

            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(servletOutputStream);

            int chunk = 0;
            while ((chunk = bufferedInputStream.read()) != -1) {
                bufferedOutputStream.write(chunk);
            }

            bufferedInputStream.close();
            fileInputStream.close();
            bufferedOutputStream.close();
            servletOutputStream.close();
        } catch (IOException ex) {
            LOGGER.error(DemoUtil.class.getName() + " Audio I/O Error Message Logged !!!", ex.getMessage(), ex);
        }
    }
}
