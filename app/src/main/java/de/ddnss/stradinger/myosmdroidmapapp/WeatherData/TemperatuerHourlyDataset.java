package de.ddnss.stradinger.myosmdroidmapapp.WeatherData;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TemperatuerHourlyDataset {
    private ArrayList<String> time;
    private Double [] temperature_2m;
    private Integer[] relativehumidity_2m;
    private Double[] windspeed_10m;

    public ArrayList<String> getTime() {
        return time;
    }

    public void setTime(ArrayList<String> time) {
        this.time = time;
    }

    public Double[] getTemperature_2m() {
        return temperature_2m;
    }

    public void setTemperature_2m(Double[] temperature_2m) {
        this.temperature_2m = temperature_2m;
    }

    public Integer[] getRelativehumidity_2m() {
        return relativehumidity_2m;
    }

    public void setRelativehumidity_2m(Integer[] relativehumidity_2m) {
        this.relativehumidity_2m = relativehumidity_2m;
    }

    public Double[] getWindspeed_10m() {
        return windspeed_10m;
    }

    public void setWindspeed_10m(Double[] windspeed_10m) {
        this.windspeed_10m = windspeed_10m;
    }
}
