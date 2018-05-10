package com.spark.twitter.analysis;

import com.spark.twitter.file.handler.FilePropertyManager;
import com.spark.twitter.utils.TwitterProperties;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.slf4j.LoggerFactory;


/**
 * Twitter Sentiment Analysis Class
 *
 * @author ritesh
 * @since 05/09/2018
 */
public class TwitterSentimentAnalysis {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(TwitterSentimentAnalysis.class);

    public static void main(String[] args) {
        //setting the logger off for spark
        Logger.getLogger("org").setLevel(Level.OFF);

        //setting twitter properties
        TwitterProperties.setProperties();

        JavaStreamingContext javaStreamingContext = new JavaStreamingContext(new SparkConf().setAppName("Twitter Sentiment Analysis"),
                new Duration(1000));


    }
}
