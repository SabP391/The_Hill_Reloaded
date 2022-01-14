package com.example.thehillreloaded.Model;

public class FirebaseUserDataAccount {

    private String email;
    private String displayName;
    private String phoneNumber;
    private String providerId;
    private String uId;
    private String tenantId;

    public FirebaseUserDataAccount(String email, String displayName, String phoneNumber, String providerId, String uId, String tenantId) {
        this.email = email;
        this.displayName = displayName;
        this.phoneNumber = phoneNumber;
        this.providerId = providerId;
        this.uId = uId;
        this.tenantId = tenantId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) { this.providerId = providerId;
    }

    public String getuId() {return uId; }

    public void setuId(String uId) { this.uId = uId; }

    public String getTenantId() { return tenantId; }

    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
}
