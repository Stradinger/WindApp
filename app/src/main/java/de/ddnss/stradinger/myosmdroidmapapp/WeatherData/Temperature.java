package de.ddnss.stradinger.myosmdroidmapapp.WeatherData;

import com.google.gson.annotations.SerializedName;

public class Temperature {
    @SerializedName("latitude")
    private Double latitude;
    @SerializedName("longitude")
    private Double longitude;
    @SerializedName("generationtime_ms")
    private Double generationtime_ms;
    @SerializedName("utc_offset_seconds")
    private Double utc_offset_seconds;
    @SerializedName("timezone")
    private String timezone;
    @SerializedName("timezone_abbreviation")
    private String timezone_abbreviation;
    @SerializedName("elevation")
    private Double elevation;

    @SerializedName("current_units")
    private TemperatureCurrentUnit current_units;
    @SerializedName("current")
    private TemperatureCurrent current;
    @SerializedName("hourly_Units")
    private TemperatureHourlyUnit hourly_Units;
    @SerializedName("hourly")
    private TemperatuerHourlyDataset hourly;

@Override
public String toString() {
    return "Temperature{" +
            "latitude=" + latitude +
            ", longitude=" + longitude +
            ", generationtime_ms=" +generationtime_ms+
            ", utc_offset_seconds=" +utc_offset_seconds+
            ", timezone='" +timezone+ '\'' +
            ", timezone_abbreviation='" +timezone_abbreviation+ '\'' +
            ", elevation" +elevation+
            '}';
}

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getGenerationtime_ms() {
        return generationtime_ms;
    }

    public void setGenerationtime_ms(Double generationtime_ms) {
        this.generationtime_ms = generationtime_ms;
    }

    public Double getUtc_offset_seconds() {
        return utc_offset_seconds;
    }

    public void setUtc_offset_seconds(Double utc_offset_seconds) {
        this.utc_offset_seconds = utc_offset_seconds;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getTimezone_abbreviation() {
        return timezone_abbreviation;
    }

    public void setTimezone_abbreviation(String timezone_abbreviation) {
        this.timezone_abbreviation = timezone_abbreviation;
    }

    public Double getElevation() {
        return elevation;
    }

    public void setElevation(Double elevation) {
        this.elevation = elevation;
    }

    public TemperatureCurrentUnit getCurrent_units() {
        return current_units;
    }

    public void setCurrent_units(TemperatureCurrentUnit unites) {
        this.current_units = unites;
    }

    public TemperatureCurrent getCurrent() {
        return current;
    }

    public void setCurrent(TemperatureCurrent current) {
        this.current = current;
    }

    public TemperatureHourlyUnit getHourly_Units() {
        return hourly_Units;
    }

    public void setHourly_Units(TemperatureHourlyUnit hourlyUnits) {
        this.hourly_Units = hourlyUnits;
    }

    public TemperatuerHourlyDataset getHourly() {
        return hourly;
    }

    public void setHourly(TemperatuerHourlyDataset dataset) {
        this.hourly = dataset;
    }


}
