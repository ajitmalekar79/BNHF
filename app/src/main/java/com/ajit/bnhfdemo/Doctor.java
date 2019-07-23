package com.ajit.bnhfdemo;

public class Doctor {


    String id;

    /**
     * doctorId is firstname + UUID
     */
    String doctorId;
    String password="";
    String firstName="";
    String lastName="";
    String qualification="";
    String registrationNo="";
    String emailId="";
    String mobile="";
    //String hospitalName;
    //String hospitalLocation;
    String visitingCharge="";
    String specialization="";
    String gender="";
    //int noflang;
    String language="";
    Util util;

    public Doctor() {

        util = new Util();

        setId(util.getUUID());


    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getVisitingCharge() {
        return visitingCharge;
    }

    public void setVisitingCharge(String visitingCharge) {
        this.visitingCharge = visitingCharge;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Doctor(String id, String doctorId, String password, String firstname, String lastname, String qualification, String registerNumber, String visitingCharges, String specialization, String gender, String languages) {
        this.id = id;
        this.doctorId = doctorId;
        this.password = password;
        this.firstName = firstname;
        this.lastName = lastname;
        this.qualification = qualification;
        this.registrationNo = registerNumber;
        this.visitingCharge = visitingCharges;
        this.specialization = specialization;
        this.gender = gender;
        this.language = languages;
    }


}
