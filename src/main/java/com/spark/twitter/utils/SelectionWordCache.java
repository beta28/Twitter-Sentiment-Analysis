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
 * Selection Word Cache
 *
 * @author ritesh
 * @since 05/09/2018
 */
public class SelectionWordCache {

    private static final Logger LOG = LoggerFactory.getLogger(SelectionWordCache.class);
    private static final String SELECTION_WORD_FILE_PATH = "src/main/resources/selection.txt";
    private static SelectionWordCache instance;

    private Set<String> selectionWordsSet;


    /**
     * Private Constructor
     */
    private SelectionWordCache() throws TwitterSentimentException {
        loadSelectionWords();
    }


    /**
     * returns the instance object
     *
     * @return instance
     */
    public static SelectionWordCache getInstance() throws TwitterSentimentException {
        if (instance == null) {
            synchronized (PositiveWordCache.class) {
                if (instance == null)
                    instance = new SelectionWordCache();
            }
        }
        return instance;
    }

    /**
     * Returns the cached set of selection words
     *
     * @return cached set of selection words
     */
    public Set<String> getSelectionWordsSet() {
        return selectionWordsSet;
    }


    /**
     * Loads all the selection words into set
     */
    private synchronized void loadSelectionWords() throws TwitterSentimentException {
        LOG.info("Loading selection words...");
        selectionWordsSet = new HashSet<>();
        try (Stream<String> stream = Files.lines(Paths.get(SELECTION_WORD_FILE_PATH))) {
            stream.forEach(word -> selectionWordsSet.add(word));
        } catch (IOException e) {
            throw new TwitterSentimentException("Exception while loading selection words",e);
        }
    }
}
