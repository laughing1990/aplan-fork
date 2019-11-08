package com.augurit.aplanmis.province.auth;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "province.plaform.access.param")
public class AuthConfig {

    private String user;

    private String password;

    private boolean useSso;

    private String authorization;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isUseSso() {
        return useSso;
    }

    public void setUseSso(boolean useSso) {
        this.useSso = useSso;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }
}
