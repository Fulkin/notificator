package com.notificator.web;

import com.notificator.model.ExpiredUsersArrayDTO;
import com.notificator.service.NotificatorService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Servlet to receive request from the team service for getting expired users and
 * send that users to the router service for lector and team lead
 */
@Controller
@RequestMapping("/notificator")
public class NotificatorController {
    private static final Logger log = getLogger(NotificatorController.class);

    @Value("${team.action.lector.uri}")
    private String teamActionLectorUri;
    @Value("${router.action.lector.uri}")
    private String routerActionLectorUri;
    @Value("${team.action.teamlead.uri}")
    private String teamActionTeamLeadUri;
    @Value("${router.action.teamlead.uri}")
    private String routerActionTeamLeadUri;

    @Autowired
    private NotificatorService notificatorService;

    @GetMapping("/notificator_lector")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void notifyLector() {
        ExpiredUsersArrayDTO expUsers = getExpiredUsers(teamActionLectorUri);
        sendExpiredUsers(expUsers, routerActionLectorUri);
    }

    @GetMapping("/notificator_team_lead")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void notifyTeamLeader() {
        ExpiredUsersArrayDTO expUsers = getExpiredUsers(teamActionTeamLeadUri);
        sendExpiredUsers(expUsers, routerActionTeamLeadUri);
    }

    private ExpiredUsersArrayDTO getExpiredUsers(String uri) {
        log.info("get response from team service with uri {}:", uri);
        ExpiredUsersArrayDTO allUsersFromTeam = notificatorService.getAllUsersFromTeam(uri);
        if (allUsersFromTeam == null) {
            log.info("expired users is null");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return allUsersFromTeam;
    }

    private void sendExpiredUsers(ExpiredUsersArrayDTO expUsers, String uri) {
        log.info("send request to router service with uri: {}", uri);
        notificatorService.sendUsersToRouter(expUsers, uri);
    }
}
