package com.ajit.bnhfdemo;

public class Packet {


    /**
     *
     * @param userId
     * username is use as the userId for the Packet
     *
     */
    String userId;
    String id;
    double latitude;
    double longitude;
    long time;
    Util util;

    public Packet() {

        util = new Util();

        setId(util.getUUID());

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
