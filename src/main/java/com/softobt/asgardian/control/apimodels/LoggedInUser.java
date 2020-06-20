package com.softobt.asgardian.control.apimodels;

/**
 * @author aobeitor
 * @since 6/8/20
 */

public class LoggedInUser {
    private String username;
    private String domain;

    public LoggedInUser(String username, String domain) {
        this.username = username;
        this.domain = domain;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
