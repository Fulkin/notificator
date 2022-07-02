package com.teamservice.notificator.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
public class BackgroundJobManager implements ServletContextListener {

    private ScheduledExecutorService scheduler;
    private String notificatorUrl;
    /**
     * https://stackoverflow.com/questions/20387881/how-to-run-certain-task-every-day-at-a-particular-time-using-scheduledexecutorse
     */

    private void initProperties() {
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("properties\\connection.properties")) {
            Properties props = new Properties();
            props.load(is);
            notificatorUrl = props.getProperty("notificator.url");
        } catch (IOException e) {
            throw new IllegalStateException("Invalid config file");
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {

        scheduler = Executors.newSingleThreadScheduledExecutor();
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Europe/Moscow"));
        ZonedDateTime nextRun = now.withHour(0).withMinute(1).withSecond(0);
        //ZonedDateTime nextRun = now.withHour(22).withMinute(0).withSecond(0);
        if(now.compareTo(nextRun) > 0) {
            nextRun = nextRun.plusMinutes(1);
            //nextRun = nextRun.plusDays(1);
        }

        Duration duration = Duration.between(now, nextRun);
        long initialDelay = duration.getSeconds();

        scheduler.scheduleAtFixedRate(new NoTrackerUsers(notificatorUrl + "/notificator_team_lead"),
                initialDelay,
                TimeUnit.MINUTES.toSeconds(1),
                //TimeUnit.DAYS.toSeconds(1),
                TimeUnit.SECONDS);

        scheduler.scheduleAtFixedRate(new NoTrackerUsers(notificatorUrl + "/notificator_lector"),
                initialDelay,
                TimeUnit.MINUTES.toSeconds(1),
                //TimeUnit.DAYS.toSeconds(1),
                TimeUnit.SECONDS);
        //scheduler.scheduleAtFixedRate(new NoTrackerUsers(), 0, 10, TimeUnit.SECONDS);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        scheduler.shutdownNow();
    }
}
