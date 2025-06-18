package com.ranjit.gatepass.models;

public class GatePassModel {
    private String reason;
    private String note;
    private String fromDate;
    private String toDate;
    private String status;
    private String appliedDate;

    public GatePassModel(String reason, String fromDate, String toDate, String status,String note,String appliedDate) {
        this.reason = reason;
        this.fromDate = fromDate;
        this.note=note;
        this.toDate = toDate;
        this.status = status;
        this.appliedDate = appliedDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public String getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(String appliedDate) {
        this.appliedDate = appliedDate;
    }
}
