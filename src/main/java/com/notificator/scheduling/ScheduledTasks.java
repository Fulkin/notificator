package com.notificator.scheduling;

import com.notificator.service.NotificatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Class that performs notification through the {@code NotificatorService} every day at the appointed time
 */
@Component
@EnableScheduling
public class ScheduledTasks {

    @Autowired
    NotificatorService notificatorService;

    /**
     * Calling {@code NotificatorService} interface methods with cron
     */
    @Scheduled(cron = "0 0 22 * * *")
    public void notifyLectorAndTeamLead() {
        notificatorService.getAndSendExpiredUsersForLector();
        notificatorService.getAndSendExpiredUsersForTeamLead();
    }
}
