package com.example.scan;

public class employclass {

    String name, email, mobileNumber, age, role, experience, address, qualification,status;

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

    public String getmobileNumber() {
        return mobileNumber;
    }

    public void setmobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getage() {
        return age;
    }

    public void setage(String age) {
        this.age = age;
    }
    public String getrole() {
        return role;
    }

    public void setrole(String role) {
        this.role = role;
    }

    public String getqualification() {
        return qualification;
    }

    public void setqualification(String qualification) {
        this.qualification = qualification;
    }

    public String getexperience() {
        return experience;
    }

    public void setexperience(String experience) {
        this.experience = experience;
    }

    public String getaddressd() {
        return address;
    }

    public void setaddress(String address) {
        this.address = address;
    }
    public String getstatus() {
        return status;
    }

    public void setstatus(String status) {
        this.status = status;
    }


    public employclass(String name, String email, String mobileNumber, String age,String role, String qualification, String experience, String address, String status) {
        this.name = name;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.age = age;
        this.role = role;
        this.qualification = qualification;
        this.experience = experience;
        this.address = address;
        this.status = status;
    }

    public employclass() {
    }
}