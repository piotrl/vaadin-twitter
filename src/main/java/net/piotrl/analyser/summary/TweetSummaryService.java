package net.piotrl.analyser.summary;

import net.piotrl.dao.Tweet;
import net.piotrl.analyser.score.TweetScore;
import net.piotrl.analyser.score.TweetScoreService;
import net.piotrl.analyser.words.WordDatabase;
import net.piotrl.analyser.words.WordSemantic;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.toList;
import static net.piotrl.analyser.words.Emotion.*;

public class TweetSummaryService {

    private List<WordSemantic> semantics;
    private TweetScoreService service;

    public void TweetService() {
        this.semantics = new WordDatabase().importDb();
        Collections.shuffle(this.semantics);
        service = new TweetScoreService(this.semantics);
    }

    public List<DaySummary> calcDaySummaries(List<Tweet> tweets) {
        List<TweetScore> scores = getTweetScores(tweets);
        List<LocalDate> days = scores.stream()
                .map(TweetScore::getTweet)
                .map(Tweet::getDay)
                .distinct()
                .collect(toList());

        return days.stream()
                .map(day -> service.calcDaySummary(day, scores))
                .collect(toList());
    }

    private List<TweetScore> getTweetScores(List<Tweet> pisTweets) {
        Assert.state(!pisTweets.isEmpty());

        TweetScoreService service = new TweetScoreService(semantics);
        return pisTweets.stream()
                .map(service::calcTweet)
                .collect(toList());
    }

    public void printSummary(List<TweetScore> classifiedTweets) {
        double size = classifiedTweets.size();
        AtomicInteger happy = new AtomicInteger();
        AtomicInteger anger = new AtomicInteger();
        AtomicInteger sadness = new AtomicInteger();
        AtomicInteger fear = new AtomicInteger();
        AtomicInteger disgust = new AtomicInteger();
        AtomicInteger neutral = new AtomicInteger();

        classifiedTweets.stream()
                .map(TweetScore::getEmotion)
                .forEach(emotion -> {
                    if (emotion.equals(ANGER)) {
                        anger.incrementAndGet();
                    }
                    if (emotion.equals(SADNESS)) {
                        sadness.incrementAndGet();
                    }
                    if (emotion.equals(HAPPINESS)) {
                        happy.incrementAndGet();
                    }
                    if (emotion.equals(FEAR)) {
                        fear.incrementAndGet();
                    }
                    if (emotion.equals(DISGUST)) {
                        disgust.incrementAndGet();
                    }
                    if (emotion.equals(UNCLASSIFIED)) {
                        neutral.incrementAndGet();
                    }
                });

        System.out.println("HAPPINESS: " + (happy.get() / size) * 100 + "%");
        System.out.println("ANGER: " + (anger.get() / size) * 100 + "%");
        System.out.println("SADNESS: " + (sadness.get() / size) * 100 + "%");
        System.out.println("FEAR: " + (fear.get() / size) * 100 + "%");
        System.out.println("DISGUST: " + (disgust.get() / size) * 100 + "%");
        System.out.println("NEUTRAL: " + (neutral.get() / size) * 100 + "%");
    }
}
