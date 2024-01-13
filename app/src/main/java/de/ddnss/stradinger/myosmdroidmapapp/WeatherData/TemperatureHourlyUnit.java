package de.ddnss.stradinger.myosmdroidmapapp.WeatherData;

public class TemperatureHourlyUnit {

    // Einheiten der stündlichen Werte
    private String hourlyTimeUnits; //	"iso8601"
    private String hourlyTemperature_2mUnits; //	"°C"
    private String hourlyRelativehumidity_2m; //	"%"
    private String hourlyWindspeed_10mUnits; //	"km/h"

    public String getHourlyTimeUnits() {
        return hourlyTimeUnits;
    }

    public void setHourlyTimeUnits(String hourlyTimeUnits) {
        this.hourlyTimeUnits = hourlyTimeUnits;
    }

    public String getHourlyTemperature_2mUnits() {
        return hourlyTemperature_2mUnits;
    }

    public void setHourlyTemperature_2mUnits(String hourlyTemperature_2mUnits) {
        this.hourlyTemperature_2mUnits = hourlyTemperature_2mUnits;
    }

    public String getHourlyRelativehumidity_2m() {
        return hourlyRelativehumidity_2m;
    }

    public void setHourlyRelativehumidity_2m(String hourlyRelativehumidity_2m) {
        this.hourlyRelativehumidity_2m = hourlyRelativehumidity_2m;
    }

    public String getHourlyWindspeed_10mUnits() {
        return hourlyWindspeed_10mUnits;
    }

    public void setHourlyWindspeed_10mUnits(String hourlyWindspeed_10mUnits) {
        this.hourlyWindspeed_10mUnits = hourlyWindspeed_10mUnits;
    }
}
