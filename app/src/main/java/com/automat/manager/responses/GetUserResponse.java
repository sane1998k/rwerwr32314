package com.automat.manager.responses;

public class GetUserResponse {
    private String id;
    private String token;
    private String login;
    private String role;
    private String fullFIO;
    private String isOnline;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFullFIO() {
        return fullFIO;
    }

    public void setFullFIO(String fullFIO) {
        this.fullFIO = fullFIO;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }


}
