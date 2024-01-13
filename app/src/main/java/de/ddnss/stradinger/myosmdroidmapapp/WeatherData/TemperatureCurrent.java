package de.ddnss.stradinger.myosmdroidmapapp.WeatherData;

public class TemperatureCurrent {
    // aktuelle Werte
    private String actTime;  //	"2023-10-29T07:15"
    private Integer interval; //	900
    private Double temperature_2m; //	10.5
    private Double windspeed_10m; //	13.4

    public String getActTime() {
        return actTime;
    }

    public void setActTime(String actTime) {
        this.actTime = actTime;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public Double getTemperature_2m() {
        return temperature_2m;
    }

    public void setTemperature_2m(Double temperature_2m) {
        this.temperature_2m = temperature_2m;
    }

    public Double getWindspeed_10m() {
        return windspeed_10m;
    }

    public void setWindspeed_10m(Double windspeed_10m) {
        this.windspeed_10m = windspeed_10m;
    }
}
