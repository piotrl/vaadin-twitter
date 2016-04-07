package net.piotrl.analyser.summary;

import net.piotrl.analyser.score.TweetScore;
import net.piotrl.analyser.score.TweetScoreService;
import net.piotrl.analyser.words.WordDatabase;
import net.piotrl.analyser.words.WordSemantic;
import net.piotrl.dao.Tweet;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class TweetSummaryService {

    private List<WordSemantic> semantics;
    private TweetScoreService service;

    public TweetSummaryService() {
        this.semantics = new WordDatabase().importDb();
        Collections.shuffle(this.semantics);
        service = new TweetScoreService(this.semantics);
    }

    public DaySummary calcSummaries(List<Tweet> tweets) {
        List<TweetScore> scores = getTweetScores(tweets);

        return service.calcSummary(scores);
    }

    private List<TweetScore> getTweetScores(List<Tweet> pisTweets) {
        Assert.state(!pisTweets.isEmpty());

        TweetScoreService service = new TweetScoreService(semantics);
        return pisTweets.stream()
                .map(service::calcTweet)
                .collect(toList());
    }
}
