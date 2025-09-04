package com.example.boxdelivery.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "boxes", uniqueConstraints = @UniqueConstraint(columnNames = "txref"))
public class Box {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false, unique = true)
    @Size(max = 20)
    @NotBlank
    private String txref;

    @Max(500)
    @Positive
    private int weightLimit; // grams, max 500

    @Min(0)
    @Max(100)
    private int batteryCapacity; // percentage 0-100

    @Enumerated(EnumType.STRING)
    private BoxState state = BoxState.IDLE;

    @Min(2)
    private int cameras;

    @OneToMany(mappedBy = "box", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Item> items = new ArrayList<>();

    public Long getId() { return id; }
    public String getTxref() { return txref; }
    public void setTxref(String txref) { this.txref = txref; }
    public int getWeightLimit() { return weightLimit; }
    public void setWeightLimit(int weightLimit) { this.weightLimit = weightLimit; }
    public int getBatteryCapacity() { return batteryCapacity; }
    public void setBatteryCapacity(int batteryCapacity) { this.batteryCapacity = batteryCapacity; }
    public BoxState getState() { return state; }
    public void setState(BoxState state) { this.state = state; }
    public List<Item> getItems() { return items; }
    public int getCurrentLoadWeight() {
        return items.stream().mapToInt(Item::getWeight).sum();
    }
    public void addItem(Item item) {
        item.setBox(this);
        this.items.add(item);
    }

    public int getCameras() {
        return cameras;
    }

    public void setCameras(int cameras) {
        this.cameras = cameras;
    }
}
