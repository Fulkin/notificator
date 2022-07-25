package com.notificator.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Properties for team service
 */
@Component
@ConfigurationProperties(prefix = "notificator.team")
public class TeamProperties {
    private String uri;
    private String actionTeamleadUri;
    private String actionLectorUri;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getActionTeamleadUri() {
        return actionTeamleadUri;
    }

    public void setActionTeamleadUri(String actionTeamleadUri) {
        this.actionTeamleadUri = actionTeamleadUri;
    }

    public String getActionLectorUri() {
        return actionLectorUri;
    }

    public void setActionLectorUri(String actionLectorUri) {
        this.actionLectorUri = actionLectorUri;
    }
}
