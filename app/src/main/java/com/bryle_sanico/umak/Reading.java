package com.bryle_sanico.umak;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Reading implements Serializable {
    public Integer id, device_id;
    public Double temperature, humidity, light_intensity, soil_moisture, water_level;

    public Reading(int id, int device_id, double temperature, double humidity, double light_intensity, double soil_moisture, double water_level) {
        this.id = id;
        this.device_id = device_id;
        this.temperature = temperature;
        this.humidity = humidity;
        this.light_intensity = light_intensity;
        this.soil_moisture = soil_moisture;
        this.water_level = water_level;
    }

    @NonNull
    @Override
    public String toString() {
        return "Reading{id=" + id +
                ", device_id='" + device_id + '\'' +
                ", temperature='" + temperature + '\'' +
                ", humidity='" + humidity + '\'' +
                ", light_intensity='" + light_intensity + '\'' +
                ", soil_moisture='" + soil_moisture + '\'' +
                ", water_level='" + water_level + '\'' +
                '}';
    }
}
