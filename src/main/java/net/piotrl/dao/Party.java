package net.piotrl.dao;

import net.piotrl.analyser.summary.Summary;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Party extends Summary {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private int tweetsSize;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTweetsSize() {
        return tweetsSize;
    }

    public void setTweetsSize(int tweetsSize) {
        this.tweetsSize = tweetsSize;
    }
}
