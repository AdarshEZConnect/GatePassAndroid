package com.ranjit.gatepass.models;

public class GatePassRequest {
    private String passType;
    private String reason;
    private String fromDateTime;
    private String toDateTime;
    private int userId;

    public GatePassRequest(String passType, String reason, String fromDateTime, String toDateTime, int userId) {
        this.passType = passType;
        this.reason = reason;
        this.fromDateTime = fromDateTime;
        this.toDateTime = toDateTime;
        this.userId = userId;
    }

    public String getPassType() {
        return passType;
    }

    public void setPassType(String passType) {
        this.passType = passType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getFromDateTime() {
        return fromDateTime;
    }

    public void setFromDateTime(String fromDateTime) {
        this.fromDateTime = fromDateTime;
    }

    public String getToDateTime() {
        return toDateTime;
    }

    public void setToDateTime(String toDateTime) {
        this.toDateTime = toDateTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    // Getters and Setters (optional if using Gson)
}
