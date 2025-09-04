package com.example.boxdelivery.dto;

import jakarta.validation.constraints.*;

public class ItemDTO {
    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "name may contain letters, numbers, hyphen '-' and underscore '_' only")
    private String name;

    @Positive
    private int weight;

    @NotBlank
    @Pattern(regexp = "^[A-Z0-9_]+$", message = "code may contain UPPERCASE letters, numbers and underscore '_' only")
    private String code;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getWeight() { return weight; }
    public void setWeight(int weight) { this.weight = weight; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}
