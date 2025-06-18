package com.ranjit.gatepass.models;

public class Department {
    private int departmentId;
    private String departmentName;

    public int getDepartmentId() {
        return departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }
    @Override
    public String toString() {
        return departmentName; // 🔥 this makes Spinner show name, not object ref
    }
}
