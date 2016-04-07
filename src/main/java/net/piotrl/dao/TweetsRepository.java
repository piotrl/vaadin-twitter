package net.piotrl.dao;

import net.piotrl.analyser.scrapper.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TweetsRepository extends JpaRepository<Tweet, Long> {

    List<Tweet> findByPartyNameStartsWithIgnoreCase(String partyName);
}
