package com.diabolo.servlet.demo;

import com.diabolo.security.SessionValidator;
import com.diabolo.util.DemoUtil;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet("/DemoUpload")
@MultipartConfig(fileSizeThreshold = 6291456, // 6 MB
                maxFileSize = 10485760L, // 10 MB
                maxRequestSize = 11534336L // 11 MB
)
//@MultipartConfig() // !! TESTING ONLY !!
public class DemoUploadServlet extends HttpServlet {

    private static final String SEPARATOR = File.separator;
    private static String storageDir;
    private static String workDir;

    @Override
    public void init() throws ServletException {
        workDir = getServletContext().getAttribute(ServletContext.TEMPDIR).toString() + SEPARATOR;
        storageDir = System.getProperty("java.io.tmpdir") + SEPARATOR + "DonDiablo" + SEPARATOR;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = new PrintWriter(response.getOutputStream(), true);
        response.setContentType("text/plain");

        HttpSession session;
        SessionValidator validator = new SessionValidator(request, response);
        if (!(validator.isValidSession()) || !(validator.isValidCookies())) {
            writer.println("<font color=red>Session expired.</font>");
            return;
        }
        session = validator.getSession();

        String inputTitle = request.getParameter("title");
        //long startTime = System.currentTimeMillis();
        //writer.println("Start time: " + startTime);

        boolean isMultipart = request.getContentType() != null &&
                request.getContentType().toLowerCase().contains("multipart/form-data");
        if (!isMultipart) {
            //Todo: redirect to invalid form input error page
            writer.println("<font color=red>Not all fields match required input patterns.</font>");
            return;
        }
        try {
            Part part = request.getPart("file");
            if ((part == null) || (part.getSize() <= 0)) {
                //Todo: redirect to empty upload error page
                writer.println("<font color=red>Empty upload found.</font>");
                return;
            }

            String contentType = part.getContentType();
            if (!DemoUtil.isValidContentType(contentType)) {
                //Todo: redirect to invalid content-type error page
                writer.println("<font color=red>Invalid content-type found.</font>");
                return;
            }

            String origFileName = part.getSubmittedFileName();
            String origFilePath = workDir + origFileName;
            part.write(origFilePath); // Writing the file to temp location and using original filename
            String outputFileName = DemoUtil.createUniqueFileName(50);
            Map<String, Object> audioMap = DemoUtil.convertDemo(workDir, origFileName, outputFileName);
            if (audioMap == null) {
                //Todo: redirect to converting audio error page
                writer.println("<font color=red>Error while converting audio.</font>");
                return;
            }

            audioMap.put("title", inputTitle);
            audioMap.put("userid", session.getAttribute("userid"));

            if (!DemoUtil.insertDemoIntoDatabase(audioMap)) {
                //Todo: redirect to inserting data into database error page
                writer.println("<font color=red>Error while inserting data into database.</font>");
                return;
            }

            //writer.println("File uploaded successfully!");
            //long endTime = System.currentTimeMillis();
            //writer.println("End time: " + endTime);
            //writer.println("Run time: " + (endTime - startTime));
            if (!DemoUtil.createStorageDirectory(storageDir)) {
                //Todo: redirect to create storage directory error page
                writer.println("<font color=red>Error creating storage directory.</font>");
                return;
            }

            if (!DemoUtil.moveFile(workDir + outputFileName, storageDir + outputFileName)) {
                //Todo: redirect to move file error page
                writer.println("<font color=red>Error moving file.</font>");
                return;
            }

            if (!DemoUtil.deleteFile(origFilePath)) {
                //Todo: redirect to delete file error page
                writer.println("<font color=red>Error deleting file.</font>");
                return;
            }
            Thread.sleep(3000);
            writer.println("something something something darkside");
        } catch (IllegalStateException ex) {
            writer.println("File exceeded allowed size limit!");
            //response.sendError(HttpServletResponse.SC_BAD_REQUEST); // HTTP 400
        } catch (InterruptedException ex) {
            writer.println("Sooo tired, going to sleep");
        }
    }
}
