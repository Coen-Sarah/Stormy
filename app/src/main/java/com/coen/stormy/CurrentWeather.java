package com.coen.stormy;

import android.annotation.SuppressLint;
import android.content.Context;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public class CurrentWeather {
    private String locationLabel;
    private String icon;
    private String localTime;
    private double temperature;
    private double humidity;
    private double precipChance;
    private String summary;
    private String timeZone;

    public CurrentWeather() {
    }

    public CurrentWeather(String locationLabel, String icon, String localTime, double temperature, double humidity, double precipChance, String summary, String timeZone) {
        this.locationLabel = locationLabel;
        this.icon = icon;
        this.localTime = localTime;
        this.temperature = temperature;
        this.humidity = humidity;
        this.precipChance = precipChance;
        this.summary = summary;
        this.timeZone = timeZone;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getLocationLabel() {
        return locationLabel;
    }

    public void setLocationLabel(String locationLabel) {
        this.locationLabel = locationLabel;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @SuppressLint("NewApi")
    public String getLocalTime(){
        if (localTime == null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
            //formatter.setTimeZone(TimeZone.getTimeZone(timeZone));
            ZonedDateTime now = ZonedDateTime.now(ZoneId.of(timeZone));
            return formatter.format(now);
        }else{
            return localTime;
        }
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getPrecipChance() {
        return precipChance;
    }

    public void setPrecipChance(double precipChance) {
        this.precipChance = precipChance;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

}
