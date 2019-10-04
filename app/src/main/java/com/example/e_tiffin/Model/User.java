package com.example.e_tiffin.Model;

public class User {

    private String Id;
    private String Name;
    private String Password;
    private String Phone;
    private String IsStaff;


    public User(String id, String name, String password) {
        Id = id;
        Name = name;
        Password = password;
        IsStaff="false";
    }

    public User() {
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getIsStaff() {
        return IsStaff;
    }

    public void setIsStaff(String isStaff) {
        IsStaff = isStaff;
    }
}
