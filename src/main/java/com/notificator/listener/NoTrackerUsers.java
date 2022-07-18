package com.notificator.listener;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Thread that pings {@code NotificatorLectorServlet} and {@code NotificatorTeamLeadServlet} servlets
 */
public class NoTrackerUsers implements Runnable {

    private static final String USER_AGENT = "Mozilla/5.0";
    /**
     * The url that the stream is accessing
     */
    private final String url;

    /**
     * Constructor for url definition
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
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
