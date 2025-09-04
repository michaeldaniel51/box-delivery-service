package com.example.boxdelivery.dto;

import jakarta.validation.constraints.*;

public class CreateBoxRequest {
    @NotBlank
    @Size(max = 20)
    private String txref;

    @Max(500)
    @Positive
    private int weightLimit;

    @Min(0)
    @Max(100)
    private int batteryCapacity;

    @Min(2)
    private int cameras;

    public String getTxref() { return txref; }
    public void setTxref(String txref) { this.txref = txref; }
    public int getWeightLimit() { return weightLimit; }
    public void setWeightLimit(int weightLimit) { this.weightLimit = weightLimit; }
    public int getBatteryCapacity() { return batteryCapacity; }
    public void setBatteryCapacity(int batteryCapacity) { this.batteryCapacity = batteryCapacity; }

    public int getCameras() {
        return cameras;
    }

    public void setCameras(int cameras) {
        this.cameras = cameras;
    }
}
