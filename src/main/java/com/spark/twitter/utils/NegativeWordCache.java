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
 * Negative Word Cache
 *
 * @author ritesh
 * @since 05/09/2018
 */
public class NegativeWordCache {

    private static final Logger LOG = LoggerFactory.getLogger(NegativeWordCache.class);
    private static final String NEGATIVE_WORD_FILE_PATH = "src/resources/neg-words.txt";
    private static NegativeWordCache instance;

    private Set<String> negativeWordsSet;


    /**
     * Private Constructor
     */
    private NegativeWordCache() throws TwitterSentimentException {
        loadNegativeWords();
    }


    /**
     * returns the instance object
     *
     * @return instance
     */
    public static NegativeWordCache getInstance() throws TwitterSentimentException {
        if (instance == null) {
            synchronized (NegativeWordCache.class) {
                if (instance == null)
                    instance = new NegativeWordCache();
            }
        }
        return instance;
    }

    /**
     * Returns the cached set of negative words
     *
     * @return cached set of negative words
     */
    public Set<String> getNegativeWordsSet() {
        return negativeWordsSet;
    }


    /**
     * Loads all the negative words into set
     */
    private synchronized void loadNegativeWords() throws TwitterSentimentException {
        LOG.info("Loading negative words...");
        negativeWordsSet = new HashSet<>();
        try (Stream<String> stream = Files.lines(Paths.get(NEGATIVE_WORD_FILE_PATH))) {
            stream.forEach(word -> negativeWordsSet.add(word));
        } catch (IOException e) {
            throw new TwitterSentimentException("Exception while loading Negative words");
        }
    }
}
