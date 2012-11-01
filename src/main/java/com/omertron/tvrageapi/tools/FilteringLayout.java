/*
 *      Copyright (c) 2004-2012 Stuart Boston
 *
 *      This software is licensed under a Creative Commons License
 *      See the LICENCE.txt file included in this package
 *
 *      For any reuse or distribution, you must make clear to others the
 *      license terms of this work.
 */
package com.omertron.tvrageapi.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Log4J Filtering routine to remove API keys from the output
 * @author Stuart.Boston
 *
 */
public class FilteringLayout extends PatternLayout {
    private static Pattern apiKeys = Pattern.compile("DO_NOT_MATCH");

    public static void addApiKey(String apiKey) {
        apiKeys = Pattern.compile(apiKey);
    }

    /**
     * Extend the format to remove the API_KEYS from the output
     * @param event
     * @return
     */
    @Override
    public String format(LoggingEvent event) {
        if (event.getMessage() instanceof String) {
            String message = event.getRenderedMessage();

            Matcher matcher = apiKeys.matcher(message);
            if (matcher.find()) {
                String maskedMessage = matcher.replaceAll("[APIKEY]");

                Throwable throwable = event.getThrowableInformation() != null ?
                        event.getThrowableInformation().getThrowable() : null;

                LoggingEvent maskedEvent = new LoggingEvent(event.fqnOfCategoryClass,
                        Logger.getLogger(event.getLoggerName()), event.timeStamp,
                        event.getLevel(), maskedMessage, throwable);

                return super.format(maskedEvent);
            }
        }
        return super.format(event);
    }
}
