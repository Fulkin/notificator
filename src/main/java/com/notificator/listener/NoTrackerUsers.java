package com.notificator.listener;

import org.slf4j.Logger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Thread that pings {@code NotificatorLectorServlet} and {@code NotificatorTeamLeadServlet} servlets
 */
public class NoTrackerUsers implements Runnable {

    private static final Logger log = getLogger(NoTrackerUsers.class);
    private static final String USER_AGENT = "Mozilla/5.0";
    /**
     * The url that the stream is accessing
     */
    private final String url;

    /**
     * Constructor for url definition
     *
     * @param url - url that the stream is accessing
     */
    public NoTrackerUsers(String url) {
        this.url = url;
    }

    /**
     * Method for implementing a thread call to the url
     */
    @Override
    public void run() {
        try {
            log.info("run thread method");
            URL obj = new URL(url);
            log.info("connect to url {}", url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);
            log.info("get SOAP message with response status: {}", con.getResponseCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
