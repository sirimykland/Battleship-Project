package com.battleship.controller.firebase;

/**
 * POJO for managing the REST requests used in authentication
 */
public class AuthResponse {
    private String idToken;
    private String email;
    private String refreshToken;
    private String expiresIn;
    private String localId;
    private boolean registered = false;

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    @Override
    public String toString(){
        return "AuthResponse {" +
                "\n\tidToken: " + idToken +
                "\n\temail: " + email +
                "\n\trefreshToken: " + refreshToken +
                "\n\texpiresIn: " + expiresIn +
                "\n\tlocalId: " + localId +
                "\n\tregistered: " + registered +
                "\n}";
    }
}
