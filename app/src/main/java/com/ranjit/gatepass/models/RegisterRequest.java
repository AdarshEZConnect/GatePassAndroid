package com.ranjit.gatepass.models;

// com.ranjit.gatepass.models.RegisterRequest.java
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String gender;
    private String profileImage;
    private String userContact;
    private String parentContact;
    private int roleId;
    private int departmentId;

    public RegisterRequest(String name,
                           String email,
                           String password,
                           String gender,
                           String profileImage,
                           String userContact,
                           String parentContact,
                           int roleId,
                           int departmentId) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.profileImage = profileImage;
        this.userContact=userContact;
        this.parentContact = parentContact;
        this.roleId = roleId;
        this.departmentId = departmentId;
    }

    // Getters & Setters if needed

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getParentContact() {
        return parentContact;
    }

    public void setParentContact(String parentContact) {
        this.parentContact = parentContact;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }
}
