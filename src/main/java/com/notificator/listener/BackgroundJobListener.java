package com.notificator.listener;

import com.notificator.util.PropertiesUtil;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * A listener that, when the service starts, activates two threads via the
 * {@code Executors.newSingleThreadScheduledExecutor()} method that pings the
 * {@code NotificatorLectorServlet} and {@code NotificatorTeamLeadServlet} servlets.
 */

@WebListener
public class BackgroundJobListener implements ServletContextListener {

    /**
     * ExecutorService for scheduled threads
     */
    private ScheduledExecutorService scheduler;

    /**
     * Override method for start scheduled threads
     *
     * @param event - not used
     */
    @Override
    public void contextInitialized(ServletContextEvent event) {
        String notificatorUrl = PropertiesUtil.getProperty("notificator.url");
        scheduler = Executors.newSingleThreadScheduledExecutor();
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Europe/Moscow"));
        ZonedDateTime nextRun = now.withHour(22).withMinute(0).withSecond(0);
        if (now.compareTo(nextRun) > 0) {
            nextRun = nextRun.plusDays(1);
        }

        Duration duration = Duration.between(now, nextRun);
        long initialDelay = duration.getSeconds();

        scheduler.scheduleAtFixedRate(new NoTrackerUsers(notificatorUrl + "/notificator_team_lead"),
                initialDelay,
                TimeUnit.DAYS.toSeconds(1),
                TimeUnit.SECONDS);

        scheduler.scheduleAtFixedRate(new NoTrackerUsers(notificatorUrl + "/notificator_lector"),
                initialDelay,
                TimeUnit.DAYS.toSeconds(1),
                TimeUnit.SECONDS);
    }

    /**
     * Override method for stop ExecutorService
     *
     * @param event - not used
     */
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        scheduler.shutdownNow();
    }
}
