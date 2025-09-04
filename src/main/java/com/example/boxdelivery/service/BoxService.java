package com.example.boxdelivery.service;

import com.example.boxdelivery.dto.ItemDTO;
import com.example.boxdelivery.entity.Box;
import com.example.boxdelivery.entity.BoxState;
import com.example.boxdelivery.entity.Item;
import com.example.boxdelivery.exception.BadRequestException;
import com.example.boxdelivery.exception.NotFoundException;
import com.example.boxdelivery.repository.BoxRepository;
import com.example.boxdelivery.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BoxService {

    private final BoxRepository boxRepository;
    private final ItemRepository itemRepository;

    public BoxService(BoxRepository boxRepository, ItemRepository itemRepository) {
        this.boxRepository = boxRepository;
        this.itemRepository = itemRepository;
    }

    public Box create(Box box) {
        if (box.getWeightLimit() > 500) {
            throw new BadRequestException("Weight limit cannot exceed 500 grams");
        }
        if (box.getBatteryCapacity() < 0 || box.getBatteryCapacity() > 100) {
            throw new BadRequestException("Battery capacity must be between 0 and 100");
        }
        if (boxRepository.existsByTxref(box.getTxref())) {
            throw new BadRequestException("Box with txref already exists");
        }

        box.setState(BoxState.IDLE);
        return boxRepository.save(box);
    }

    public Box getById(Long id) {
        return boxRepository.findById(id).orElseThrow(() -> new NotFoundException("Box not found: " + id));
    }

    public List<Item> getItems(Long boxId) {
        getById(boxId); // ensure exists
        return itemRepository.findByBoxId(boxId);
    }

    public List<Box> availableForLoading() {
        // assumption: "available" means currently IDLE and battery >= 25%
        return boxRepository.findByStateAndBatteryCapacityGreaterThanEqual(BoxState.IDLE, 25);
    }

    public int getBattery(Long id) {
        return getById(id).getBatteryCapacity();
    }

    @Transactional
    public Box loadItems(Long boxId, List<ItemDTO> itemDTOs) {
        Box box = getById(boxId);

        if (box.getBatteryCapacity() < 25) {
            throw new BadRequestException("Box cannot enter LOADING state: battery below 25%");
        }

        // Enter LOADING state
        box.setState(BoxState.LOADING);

        int additionalWeight = itemDTOs.stream().mapToInt(ItemDTO::getWeight).sum();
        int current = box.getCurrentLoadWeight();
        if (current + additionalWeight > box.getWeightLimit()) {
            throw new BadRequestException("Total load weight (" + (current + additionalWeight) + "g) exceeds box limit (" + box.getWeightLimit() + "g)");
        }

        for (ItemDTO dto : itemDTOs) {
            Item item = new Item();
            item.setName(dto.getName());
            item.setWeight(dto.getWeight());
            item.setCode(dto.getCode());
            box.addItem(item);
        }

        // After loading, set to LOADED
        box.setState(BoxState.LOADED);
        return boxRepository.save(box);
    }

    public List<Box> findAll() {
        return boxRepository.findAll();
    }

    @Transactional
    public Box chargeBox(Long boxId, int newLevel) {
        Box box = getById(boxId);
        if (newLevel < box.getBatteryCapacity()) {
            throw new BadRequestException("Cannot set battery lower than current level.");
        }
        if (newLevel > 100) {
            newLevel = 100; // cap at max 100%
        }
        box.setBatteryCapacity(newLevel);
        return boxRepository.save(box);
    }


}
