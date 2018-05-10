package com.spark.twitter.utils;

import com.spark.twitter.file.handler.FilePropertyManager;


/**
 * Twitter Auth Properties Class
 *
 * @author ritesh
 * @since 05/09/2018
 */
public class TwitterProperties {

    private static final String CONSUMER_KEY = FilePropertyManager.getProperty("twitter.properties", "consumer.key");
    private static final String CONSUMER_SECRET_KEY = FilePropertyManager.getProperty("twitter.properties", "consumer.secret");
    private static final String ACCESS_TOKEN_KEY = FilePropertyManager.getProperty("twitter.properties", "access.token.key");
    private static final String ACCESS_TOKEN_SECRET = FilePropertyManager.getProperty("twitter.properties", "access.token.secret");

    /**
     * sets all the auth property for twitter app
     */
    public static void setProperties() {
        System.setProperty("twitter4j.oauth.consumerKey", CONSUMER_KEY);
        System.setProperty("twitter4j.oauth.consumerSecret", CONSUMER_SECRET_KEY);
        System.setProperty("twitter4j.oauth.accessToken", ACCESS_TOKEN_KEY);
        System.setProperty("twitter4j.oauth.accessTokenSecret", ACCESS_TOKEN_SECRET);
    }
}
