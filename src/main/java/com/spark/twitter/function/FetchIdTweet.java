package com.spark.twitter.function;


import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;
import twitter4j.Status;

import java.util.Objects;

/**
 * Class to filter and retrieve data from tweet
 *
 * @author ritesh
 * @since 05/09/2016
 */
public class FetchIdTweet implements PairFunction<Status, Long, String> {

    @Override
    public Tuple2<Long, String> call(Status status) {
        if (status != null && !Objects.isNull(status.getId()) && status.getText() != null)
            return new Tuple2<>(status.getId(), status.getText());
        return null;
    }
}
