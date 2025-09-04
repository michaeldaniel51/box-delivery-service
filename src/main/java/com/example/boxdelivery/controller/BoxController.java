package com.example.boxdelivery.controller;

import com.example.boxdelivery.dto.CreateBoxRequest;
import com.example.boxdelivery.dto.ItemDTO;
import com.example.boxdelivery.dto.LoadItemsRequest;
import com.example.boxdelivery.entity.Box;
import com.example.boxdelivery.entity.Item;
import com.example.boxdelivery.service.BoxService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/boxes")
public class BoxController {

    private final BoxService boxService;

    public BoxController(BoxService boxService) {
        this.boxService = boxService;
    }

    @PostMapping
    public ResponseEntity<Box> create(@Valid @RequestBody CreateBoxRequest req) {
        Box box = new Box();
        box.setTxref(req.getTxref());
        box.setWeightLimit(req.getWeightLimit());
        box.setBatteryCapacity(req.getBatteryCapacity());
        box.setCameras(req.getCameras());
        Box saved = boxService.create(box);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public List<Box> list() {
        return boxService.findAll();
    }

    @PostMapping("/{boxId}/load")
    public Box load(@PathVariable Long boxId, @Valid @RequestBody LoadItemsRequest request) {
        return boxService.loadItems(boxId, request.getItems());
    }

    @GetMapping("/{boxId}/items")
    public List<Item> items(@PathVariable Long boxId) {
        return boxService.getItems(boxId);
    }

    @GetMapping("/available")
    public List<Box> available() {
        return boxService.availableForLoading();
    }

    @GetMapping("/{boxId}/battery")
    public Map<String, Integer> battery(@PathVariable Long boxId) {
        return Map.of("battery", boxService.getBattery(boxId));
    }

    @PutMapping("/{boxId}/charge")
    public Box chargeBox(@PathVariable("boxId") Long boxId,
                         @RequestParam int level) {
        return boxService.chargeBox(boxId, level);
    }


}
