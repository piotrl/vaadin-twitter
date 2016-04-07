package net.piotrl.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class Tweet {

    @Id
    @GeneratedValue
    private Long id;

    private String partyName;

    private LocalDate day;

    @Column(columnDefinition = "TEXT")
    private String tweet;

    public Tweet() {
    }

    public Tweet(String partyName, LocalDate day, String tweet) {
        this.partyName = partyName;
        this.day = day;
        this.tweet = tweet;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }
}
