package com.appxone.heartrateanimationapp;

/**
 * Created by FAHAD on 2/20/2017.
 */
public class Model_HeartRate {
    String datetime, heartrate;

    public Model_HeartRate(String datetime, String heartrate) {
        this.datetime = datetime;
        this.heartrate = heartrate;
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
