package net.piotrl.imports;

import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;
import net.piotrl.VaadinInitializer;
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

public class TweetsImporter implements Upload.Receiver, Upload.SucceededListener, Upload.FailedListener {
    public File file;

    private final TweetsRepository tweetsRepository;
    private final PartyRepository partyRepository;
    private final TweetSummaryService tweetSummaryService;
    private final VaadinInitializer ui;


    public TweetsImporter(PartyRepository partyRepository,
                          TweetsRepository tweetsRepository,
                          TweetSummaryService tweetSummaryService,
                          VaadinInitializer vaadinInitializer) {
        this.tweetsRepository = tweetsRepository;
        this.partyRepository = partyRepository;
        this.tweetSummaryService = tweetSummaryService;
        this.ui = vaadinInitializer;
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

        Runnable task = () -> {
            System.out.println("Imported: " + partyName);
            List<Tweet> importedTweets = FileUtil.loadFromCsv(filename);
            tweetsRepository.save(importedTweets);
            System.out.println("Imported tweets: " + importedTweets.size());
            Summary summary = tweetSummaryService.calcSummaries(importedTweets);
            saveParty(partyName, summary, importedTweets.size());

            ui.access(() -> {
                ui.uploadInfoWindow.uploadFinished(event);
                ui.uploadInfoWindow.setClosable(true);
                ui.refreshPartiesGrid(null);
            });
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    private void saveParty(String partyName, Summary summary, int size) {
        Party party = new Party();
        party.setName(partyName);
        party.setTweetsSize(size);
        BeanUtils.copyProperties(summary, party);
        partyRepository.save(party);
    }

    @Override
    public void uploadFailed(Upload.FailedEvent event) {
        Upload component = (Upload) event.getComponent();
        component.interruptUpload();
    }
}
