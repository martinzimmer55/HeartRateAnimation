package com.appxone.heartrateanimationapp;

/**
 * Created by FAHAD on 2/20/2017.
 */
public class Model_HeartRate {
    String datetime, heartrate;
    int id;

    public Model_HeartRate(int id, String datetime, String heartrate) {
        this.id = id;
        this.datetime = datetime;
        this.heartrate = heartrate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getHeartrate() {
        return heartrate;
    }

    public void setHeartrate(String heartrate) {
        this.heartrate = heartrate;
    }
}
