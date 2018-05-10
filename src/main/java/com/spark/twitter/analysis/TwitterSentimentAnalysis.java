package com.spark.twitter.analysis;

import com.spark.twitter.exception.TwitterSentimentException;
import com.spark.twitter.function.FetchIdTweet;
import com.spark.twitter.utils.*;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.twitter.TwitterUtils;
import org.slf4j.LoggerFactory;
import scala.Tuple3;
import scala.Tuple4;
import scala.Tuple5;

import java.util.Set;


/**
 * Twitter Sentiment Analysis Class
 *
 * @author ritesh
 * @since 05/09/2018
 */
public class TwitterSentimentAnalysis {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(TwitterSentimentAnalysis.class);
    private static final String OUTPUT_FILE = "tweet-sentiment.txt";

    public static void main(String[] args) throws TwitterSentimentException {
        //setting the logger off for spark
        Logger.getLogger("org").setLevel(Level.OFF);

        //setting twitter properties
        TwitterProperties.setProperties();

        //spark streaming context
        JavaStreamingContext javaStreamingContext = new JavaStreamingContext(new SparkConf().setAppName("Twitter Sentiment Analysis"),
                new Duration(1000));

        //get set of keywords for tweet analysis
        Set<String> keywords = SelectionWordCache.getInstance().getSelectionWordsSet();

        //Creates twitter Stream
        JavaReceiverInputDStream stream = TwitterUtils.createStream(javaStreamingContext);

        //Gets new Stream of RDD of the form (tweetID, tweet)
        JavaPairDStream<Long, String> tweets = stream.mapToPair(new FetchIdTweet());

        //filter tweets based on the the keywords
        JavaPairDStream<Long, String> filteredTweets = tweets.filter(tweet -> {
            for (String key : keywords) {
                if (tweet._2().contains(key)) return true;
            }
            return false;
        });

        // Apply text filter to remove different unwanted symbols
        // outputs the stream in the format (tweetID, tweet, filteredTweet)
        JavaDStream<Tuple3<Long, String, String>> tweetsFiltered = filteredTweets.map(tweet -> {
            String filterText = tweet._2();
            filterText = filterText.replaceAll("[^a-zA-Z\\s]", "").trim().toLowerCase();
            filterText = filterText.replaceAll("(\\r\\n)", " ");
            filterText = filterText.replaceAll("(\\r|\\n)", " ");
            return new Tuple3<>(tweet._1(), tweet._2(), filterText);
        });

        //remove the stop words from each tweet and output the stream in the format (tweetID, tweet, filteredTweet)
        tweetsFiltered = tweetsFiltered.map(tweet -> {
            String filterText = tweet._3();
            Set<String> stopWords = StopWordCache.getInstance().getStopWordsSet();
            for (String word : stopWords)
                filterText = filterText.replaceAll("\\b" + word + "\\b", "");
            return new Tuple3<>(tweet._1(), tweet._2(), filterText);
        });

        //count and score positive and negative tweets
        JavaDStream<Tuple4<Long, String, Float, Float>> scoreTweets = tweetsFiltered.map(tweet -> {
            float posCount = 0;
            Set<String> positiveWords = PositiveWordCache.getInstance().getPositiveWordsSet();
            String words[] = tweet._3().split(" ");
            for (String word : words) {
                if (positiveWords.contains(word))
                    posCount++;
            }

            float negCount = 0;
            Set<String> negativeWords = NegativeWordCache.getInstance().getNegativeWordsSet();
            for (String word : words) {
                if (negativeWords.contains(word))
                    negCount++;
            }
            return new Tuple4<>(tweet._1(), tweet._2(), posCount / words.length, negCount / words.length);
        });

        //Filter out neutral/unwanted results
        JavaDStream<Tuple4<Long, String, Float, Float>> filteredScoredTweets = scoreTweets.
                filter(scoreTweet -> ((scoreTweet._3() > scoreTweet._4()) ||
                        (scoreTweet._3() < scoreTweet._4()) ||
                        ((scoreTweet._3().floatValue() == scoreTweet._4().floatValue()) &&
                                (scoreTweet._3() > 0.0 && scoreTweet._4() > 0.0))));


        //Identify sentiment and assign it to result
        //Outputs the new stream in the format (tweetID, tweet, posScore, negScore, sentiment)
        JavaDStream<Tuple5<Long, String, Float, Float, String>> result =
                filteredScoredTweets.map(tweet -> {
                    String sentiment = tweet._3() > tweet._4() ? "positive" : "negative";
                    return new Tuple5<>(tweet._1(), tweet._2(), tweet._3(), tweet._4(), sentiment);
                });

        //Write the result to fs
        result.foreachRDD(tweet -> {
            if (!tweet.isEmpty()) {
                tweet.saveAsTextFile(OUTPUT_FILE);
            }
        });
    }
}
