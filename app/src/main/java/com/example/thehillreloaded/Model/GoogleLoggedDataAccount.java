package com.example.thehillreloaded.Model;


public class GoogleLoggedDataAccount {

    private String personId;
    private String idToken;
    private String personName;
    private String personGivenName;
    private String personFamilyName;
    private String personEmail;
    private String serverAuthCode;
    private boolean flagIsLogged;

    public GoogleLoggedDataAccount(String personId, String idToken, String personName,
                                   String personGivenName, String personFamilyName, String personEmail,
                                   String serverAuthCode, boolean flagIsLogged) {
        this.personId = personId;
        this.idToken = idToken;
        this.personName = personName;
        this.personGivenName = personGivenName;
        this.personFamilyName = personFamilyName;
        this.personEmail = personEmail;
        this.serverAuthCode = serverAuthCode;
        this.flagIsLogged = flagIsLogged;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonGivenName() {
        return personGivenName;
    }

    public void setPersonGivenName(String personGivenName) {
        this.personGivenName = personGivenName;
    }

    public String getPersonFamilyName() {
        return personFamilyName;
    }

    public void setPersonFamilyName(String personFamilyName) {
        this.personFamilyName = personFamilyName;
    }

    public String getPersonEmail() {
        return personEmail;
    }

    public void setPersonEmail(String personEmail) {
        this.personEmail = personEmail;
    }

    public String getServerAuthCode() {
        return serverAuthCode;
    }

    public void setServerAuthCode(String serverAuthCode) {
        this.serverAuthCode = serverAuthCode;
    }

    public boolean isFlagIsLogged() {
        return flagIsLogged;
    }

    public void setFlagIsLogged(boolean flagIsLogged) {
        this.flagIsLogged = flagIsLogged;
    }

}
