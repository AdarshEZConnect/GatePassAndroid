package com.ranjit.gatepass.models;

public class GatePassModel {
    private String studentName;
    private String reason;
    private String fromDate;
    private String toDate;
    private String status;

    public GatePassModel(String studentName, String reason, String fromDate, String toDate, String status) {
        this.studentName = studentName;
        this.reason = reason;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.status = status;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getReason() {
        return reason;
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public String getStatus() {
        return status;
    }
}
