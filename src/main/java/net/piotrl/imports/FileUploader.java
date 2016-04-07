package net.piotrl.imports;

import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;
import net.piotrl.analyser.scrapper.Tweet;
import net.piotrl.utils.FileUtil;

import java.io.*;
import java.util.List;

public class FileUploader implements Upload.Receiver, Upload.SucceededListener {
    public File file;

    public OutputStream receiveUpload(String filename,
                                      String mimeType) {
        // Create upload stream
        FileOutputStream fos = null; // Stream to write to
        try {
            // Open the file for writing.
            file = new File("uploads/" + filename);
            fos = new FileOutputStream(file);
        } catch (final java.io.FileNotFoundException e) {
            new Notification("Could not open file<br/>",
                    e.getMessage(),
                    Notification.Type.ERROR_MESSAGE)
                    .show(Page.getCurrent());
            return null;
        }
        return fos; // Return the output stream to write to
    }

    public void uploadSucceeded(Upload.SucceededEvent event) {
        System.out.println(event.getFilename());
        List<Tweet> pisTweets = FileUtil.loadFromCsv(event.getFilename());

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // Show the uploaded file in the image viewer
//        image.setVisible(true);
//        image.setSource(new FileResource(file));
    }
};
