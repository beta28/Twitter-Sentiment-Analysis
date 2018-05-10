package com.spark.twitter.file.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;



/**
 * Class for handling the reading from property files
 *
 * @author ritesh
 * @since 05/09/2018
 */
public class FilePropertyManager {
    private static final Logger LOG = LoggerFactory.getLogger(FilePropertyManager.class);
    private static final Map<String, Properties> PROPERTIES_MAP = new HashMap<>();


    /**
     * Do Nothing
     */
    private FilePropertyManager() {
    }


    /**
     * Returns a property from a properties file.
     *
     * @param fileName name of the property file
     * @param key      name of the key
     * @return Value of the key in the file. If the key is not present then null is returned
     */
    public static String getProperty(final String fileName, final String key) {
        if (!PROPERTIES_MAP.containsKey(fileName)) {
            loadPropertyFile(fileName);
        }
        final Properties prop = PROPERTIES_MAP.get(fileName);
        String property = prop.getProperty(key);
        if (property == null) {
            LOG.warn("Property " + key + " was not present in " + fileName);
        }
        return property;
    }


    private static void loadPropertyFile(final String fileName) throws RuntimeException {
        try {
            final Properties prop = new Properties();
            prop.load(FilePropertyManager.class.getClassLoader().getResourceAsStream(fileName));
            PROPERTIES_MAP.put(fileName, prop);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
