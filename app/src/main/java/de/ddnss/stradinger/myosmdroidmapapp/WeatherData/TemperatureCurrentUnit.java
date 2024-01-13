package de.ddnss.stradinger.myosmdroidmapapp.WeatherData;

public class TemperatureCurrentUnit {

    // Einheiten
    private String time;
    private String interval;
    private String temperature_2m;
    private String windspeed_10m;


    @Override
    public String toString() {
        return "TemperatureCurrentUnit {" +
                "time='" + time + "'" +
                ", interval='" + interval + "'" +
                ", temperature_2m='" + temperature_2m + "'" +
                ", windspeed_10m='" + windspeed_10m + "'" +
                "}";
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getTemperature_2m() {
        return temperature_2m;
    }

    public void setTemperature_2m(String temperature_2m) {
        this.temperature_2m = temperature_2m;
    }

    public String getWindspeed_10m() {
        return windspeed_10m;
    }

    public void setWindspeed_10m(String windspeed_10m) {
        this.windspeed_10m = windspeed_10m;
    }
}
