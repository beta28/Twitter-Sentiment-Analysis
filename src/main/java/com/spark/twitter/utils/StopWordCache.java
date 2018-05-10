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
 * StopWord Cache Cache
 *
 * @author ritesh
 * @since 05/09/2018
 */
public class StopWordCache {

    private static final Logger LOG = LoggerFactory.getLogger(StopWordCache.class);
    private static final String STOP_WORD_FILE_PATH = "src/main/resources/stop-words.txt";
    private static StopWordCache instance;

    private Set<String> stopWordsSet;


    /**
     * Private Constructor
     */
    private StopWordCache() throws TwitterSentimentException {
        loadStopWords();
    }


    /**
     * returns the instance object
     *
     * @return instance
     */
    public static StopWordCache getInstance() throws TwitterSentimentException {
        if (instance == null) {
            synchronized (StopWordCache.class) {
                if (instance == null)
                    instance = new StopWordCache();
            }
        }
        return instance;
    }

    /**
     * Returns the cached set of negative words
     *
     * @return cached set of stop words
     */
    public Set<String> getStopWordsSet() {
        return stopWordsSet;
    }


    /**
     * Loads all the stop words into set
     */
    private synchronized void loadStopWords() throws TwitterSentimentException {
        LOG.info("Loading stop words...");
        stopWordsSet = new HashSet<>();
        try (Stream<String> stream = Files.lines(Paths.get(STOP_WORD_FILE_PATH))) {
            stream.forEach(word -> stopWordsSet.add(word));
        } catch (IOException e) {
            throw new TwitterSentimentException("Exception while loading stop words",e);
        }
    }
}
