package com.example.boxdelivery.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class LoadItemsRequest {
    @NotEmpty
    @Valid
    private List<ItemDTO> items;

    public List<ItemDTO> getItems() { return items; }
    public void setItems(List<ItemDTO> items) { this.items = items; }
}
