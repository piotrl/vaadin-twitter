package net.piotrl.utils;

import net.piotrl.dao.Tweet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class FileUtil {
    private final static String DELIMER = ";";

    public static List<Tweet> loadFromCsv(String fileName) {
        List<String> csv = loadFromFile(fileName);
        return csv.stream()
                .map(FileUtil::csvToTweet)
                .collect(toList());
    }

    private static List<String> loadFromFile(String fileName) {
        try {
            return Files.readAllLines(Paths.get("./uploads/" + fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private static Tweet csvToTweet(String csv) {
        String[]
                split = csv.split(DELIMER);
        String partyName = split[0];
        String dateText = split[1];
        String content = split[2];

        LocalDate date = LocalDate.parse(dateText, DateTimeFormatter.ISO_LOCAL_DATE);
        return new Tweet(partyName, date, content);
    }

    private static String normalizeTweet(String tweetText) {
        return tweetText
                .replaceAll("/.", "")
                .replaceAll(",", "")
                .replaceAll(";", "")
                .replaceAll("\n", "");
    }

}