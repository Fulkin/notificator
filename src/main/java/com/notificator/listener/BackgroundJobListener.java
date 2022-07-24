package com.notificator.listener;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * A listener that, when the service starts, activates two threads via the
 * {@code Executors.newSingleThreadScheduledExecutor()} method that pings the
 * {@code NotificatorLectorServlet} and {@code NotificatorTeamLeadServlet} servlets.
 */

@Component
public class BackgroundJobListener implements ServletContextListener {

    private static final Logger log = getLogger(BackgroundJobListener.class);
    /**
     * ExecutorService for scheduled threads
     */
    private ScheduledExecutorService scheduler;

    @Value("${notificator.url}")
    private String notificatorUri;

    /**
     * Override method for start scheduled threads
     *
     * @param event - not used
     */
    @Override
    public void contextInitialized(ServletContextEvent event) {
        log.info("start initialize threads listener");
        scheduler = Executors.newSingleThreadScheduledExecutor();
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Europe/Moscow"));
        ZonedDateTime nextRun = now.withHour(0).withMinute(1).withSecond(0);
        if (now.compareTo(nextRun) > 0) {
            //nextRun = nextRun.plusDays(1);
            nextRun = nextRun.plusMinutes(1);
        }

        Duration duration = Duration.between(now, nextRun);
        long initialDelay = duration.getSeconds();

        scheduler.scheduleAtFixedRate(new NoTrackerUsers(notificatorUri + "/notificator_team_lead"),
                initialDelay,
                TimeUnit.MINUTES.toSeconds(1),
                TimeUnit.SECONDS);

        scheduler.scheduleAtFixedRate(new NoTrackerUsers(notificatorUri + "/notificator_lector"),
                initialDelay,
                TimeUnit.MINUTES.toSeconds(1),
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
