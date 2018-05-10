package com.spark.twitter.exception;

/**
 * User Defined Exception class
 *
 * @author ritesh
 * @since 05/09/2018
 */
public class TwitterSentimentException extends Exception {
    /**
     * Field serialVersionUID. (value is 1)
     */
    private static final long serialVersionUID = 1L;


    /**
     * Instantiates a new TwitterSentimentException.
     */
    public TwitterSentimentException() {
        super();
    }


    /**
     * Initialises Exception with exception message and throwable cause
     *
     * @param message exception message
     * @param th      exception message
     */
    public TwitterSentimentException(String message, Throwable th) {
        super(message, th);
    }


    /**
     * Initialises Exception with all parameters
     *
     * @param message            exception message
     * @param cause              throwable cause
     * @param enableSuppression  boolean for enabling suppression
     * @param writableStackTrace boolean for printing stack trace
     */
    public TwitterSentimentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


    /**
     * Initialises Exception with exception message
     *
     * @param message exception message
     */
    public TwitterSentimentException(String message) {
        super(message);
    }


    /**
     * Initialises Exception with throwable cause
     *
     * @param cause throwable cause
     */
    public TwitterSentimentException(Throwable cause) {
        super(cause);
    }
}
