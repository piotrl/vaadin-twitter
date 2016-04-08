package net.piotrl.imports;

import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;
import net.piotrl.analyser.summary.DaySummary;
import net.piotrl.analyser.summary.Summary;
import net.piotrl.analyser.summary.TweetSummaryService;
import net.piotrl.dao.Party;
import net.piotrl.dao.PartyRepository;
import net.piotrl.dao.Tweet;
import net.piotrl.dao.TweetsRepository;
import net.piotrl.utils.FileUtil;
import org.springframework.beans.BeanUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import static com.vaadin.ui.Notification.Type.ERROR_MESSAGE;
import static com.vaadin.ui.Notification.Type.WARNING_MESSAGE;

public class TweetsImporter implements Upload.Receiver, Upload.SucceededListener {
    public File file;

    TweetsRepository tweetsRepository;
    PartyRepository partyRepository;
    TweetSummaryService tweetSummaryService;

    public TweetsImporter(PartyRepository partyRepository,
                          TweetsRepository tweetsRepository,
                          TweetSummaryService tweetSummaryService) {
        this.tweetsRepository = tweetsRepository;
        this.partyRepository = partyRepository;
        this.tweetSummaryService = tweetSummaryService;
    }

    public OutputStream receiveUpload(String filename,
                                      String mimeType) {
        if (!filename.contains(".csv")) {
            showNotification("File is not CSV", WARNING_MESSAGE);
            return null;
        }

        FileOutputStream fos;
        try {
            file = new File("uploads/" + filename);
            fos = new FileOutputStream(file);
        } catch (final FileNotFoundException e) {
            showNotification("Could not open file", ERROR_MESSAGE);
            return null;
        }
        return fos; // Return the output stream to write to
    }

    private void showNotification(String message, Notification.Type type) {
        new Notification(message, type)
                .show(Page.getCurrent());
    }

    public void uploadSucceeded(Upload.SucceededEvent event) {
        String filename = event.getFilename();
        String partyName = filename.replace(".csv", "");

        List<Tweet> importedTweets = FileUtil.loadFromCsv(filename);
        tweetsRepository.save(importedTweets);

        Summary summary = tweetSummaryService.calcSummaries(importedTweets);
        saveParty(partyName, summary, importedTweets.size());
    }

    private void saveParty(String partyName, Summary summary, int size) {
        Party party = new Party();
        party.setName(partyName);
        party.setTweetsSize(size);
        BeanUtils.copyProperties(summary, party);
        partyRepository.save(party);
    }
}
