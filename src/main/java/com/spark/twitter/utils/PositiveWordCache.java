package com.spark.twitter.utils;


import com.spark.twitter.exception.TwitterSentimentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Positive Word Cache
 *
 * @author ritesh
 * @since 05/09/2018
 */
public class PositiveWordCache {

    private static final Logger LOG = LoggerFactory.getLogger(PositiveWordCache.class);
    private static final String POSITIVE_WORD_FILE_PATH = "src/resources/pos-words.txt";
    private static PositiveWordCache instance;

    private Set<String> positiveWordsSet;


    /**
     * Private Constructor
     */
    private PositiveWordCache() throws TwitterSentimentException {
        loadPositiveWords();
    }


    /**
     * returns the instance object
     *
     * @return instance
     */
    public static PositiveWordCache getInstance() throws TwitterSentimentException {
        if (instance == null) {
            synchronized (PositiveWordCache.class) {
                if (instance == null)
                    instance = new PositiveWordCache();
            }
        }
        return instance;
    }

    /**
     * Returns the cached set of Positive words
     *
     * @return cached set of Positive words
     */
    public Set<String> getPositiveWordsSet() {
        return positiveWordsSet;
    }


    /**
     * Loads all the Positive words into set
     */
    private synchronized void loadPositiveWords() throws TwitterSentimentException {
        LOG.info("Loading Positive words...");
        positiveWordsSet = new HashSet<>();
        try (Stream<String> stream = Files.lines(Paths.get(POSITIVE_WORD_FILE_PATH))) {
            stream.forEach(word -> positiveWordsSet.add(word));
        } catch (IOException e) {
            throw new TwitterSentimentException("Exception while loading Positive words");
        }
    }
}
