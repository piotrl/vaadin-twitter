package net.piotrl.imports;

import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;
import net.piotrl.analyser.scrapper.Tweet;
import net.piotrl.dao.Party;
import net.piotrl.dao.PartyRepository;
import net.piotrl.dao.TweetsRepository;
import net.piotrl.utils.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

public class TweetsImporter implements Upload.Receiver, Upload.SucceededListener {
    public File file;

    TweetsRepository tweetsRepository;
    PartyRepository partyRepository;

    public TweetsImporter(PartyRepository partyRepository, TweetsRepository tweetsRepository) {
        this.tweetsRepository = tweetsRepository;
        this.partyRepository = partyRepository;
    }

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
        String filename = event.getFilename();
        List<Tweet> tweets = FileUtil.loadFromCsv(filename);
        tweetsRepository.save(tweets);

        saveParty(filename);
    }

    private void saveParty(String filename) {
        String partyName = filename.replace(".csv", "");
        Party party = new Party();
        party.setName(partyName);
        partyRepository.save(party);
    }
};
