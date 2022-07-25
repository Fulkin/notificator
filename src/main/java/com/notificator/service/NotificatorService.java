package com.notificator.service;

/**
 * Service layer to get expired users for further sending
 */
public interface NotificatorService {

    /**
     * Get and send expired users for lector
     */
    void getAndSendExpiredUsersForLector();

    /**
     * Get and send expired users for team lead
     */
    void getAndSendExpiredUsersForTeamLead();
}
