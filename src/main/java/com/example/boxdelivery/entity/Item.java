package com.example.boxdelivery.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "name may contain letters, numbers, hyphen '-' and underscore '_' only")
    @NotBlank
    private String name;

    @Positive
    private int weight; // grams

    @Pattern(regexp = "^[A-Z0-9_]+$", message = "code may contain UPPERCASE letters, numbers and underscore '_' only")
    @NotBlank
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "box_id")
    @JsonBackReference
    private Box box;

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getWeight() { return weight; }
    public void setWeight(int weight) { this.weight = weight; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public Box getBox() { return box; }
    public void setBox(Box box) { this.box = box; }
}
