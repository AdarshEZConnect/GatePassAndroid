package com.ranjit.gatepass.models;

public class GatePassResponse {
    private int gatePassId;
    private String passType;
    private String reason;
    private String appliedDateTime;
    private String fromDate;
    private String fromTime;
    private String toDate;
    private String toTime;
    private String userName;
    private String departmentName;
    private String status;
    private String profileImage;

    // Getters and Setters

    public int getGatePassId() {
        return gatePassId;
    }

    public void setGatePassId(int gatePassId) {
        this.gatePassId = gatePassId;
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

    public String getAppliedDateTime() {
        return appliedDateTime;
    }

    public void setAppliedDateTime(String appliedDateTime) {
        this.appliedDateTime = appliedDateTime;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public GatePassResponse(int gatePassId, String passType, String reason, String appliedDateTime, String fromDate, String fromTime, String toDate, String toTime, String userName, String departmentName, String status, String profileImage) {
        this.gatePassId = gatePassId;
        this.passType = passType;
        this.reason = reason;
        this.appliedDateTime = appliedDateTime;
        this.fromDate = fromDate;
        this.fromTime = fromTime;
        this.toDate = toDate;
        this.toTime = toTime;
        this.userName = userName;
        this.departmentName = departmentName;
        this.status = status;
        this.profileImage = profileImage;
    }
}
