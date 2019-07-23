package com.ajit.bnhfdemo;

import java.util.Date;
import java.sql.Time;

public class Hospital {


    String hospId;
    String hospName;
    String hospCity;
    double latitude;
    double longitude;

    /*String mobileNumber;
    String address;
    String information;*/
    String inTime;
    String outTime;
    String fromDate;
    String toDate;
    Doctor doctor;
    Util util;

    public Hospital() {

        util = new Util();

        setHospId(util.getUUID());
    }



    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }


    public String getHospId() {
        return hospId;
    }

    public void setHospId(String hospId) {
        this.hospId = hospId;
    }


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getHospName() {
        return hospName;
    }

    public void setHospName(String hospName) {
        this.hospName = hospName;
    }

    public String getHospCity() {
        return hospCity;
    }

    public void setHospCity(String hospCity) {
        this.hospCity = hospCity;
    }

    /*public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }*/

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public Hospital(String hospId, String hospName, String hospCity, double latitude, double longitude, String inTime, String outTime, String fromDate, String toDate) {
        this.hospId = hospId;
        this.hospName = hospName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.hospCity = hospCity;
        this.inTime = inTime;
        this.outTime = outTime;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }
}
