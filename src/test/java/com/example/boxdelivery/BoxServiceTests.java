package com.example.boxdelivery;

import com.example.boxdelivery.dto.ItemDTO;
import com.example.boxdelivery.entity.Box;
import com.example.boxdelivery.service.BoxService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
class BoxServiceTests {

    @Autowired
    private BoxService service;

    @Test
    @Transactional
    void load_within_limit_and_battery_ok() {
        Box b = new Box();
        b.setTxref("TEST_1");
        b.setWeightLimit(200);
        b.setBatteryCapacity(80);
        b = service.create(b);

        ItemDTO i1 = new ItemDTO();
        i1.setName("item_1");
        i1.setWeight(50);
        i1.setCode("I_1");

        ItemDTO i2 = new ItemDTO();
        i2.setName("item_2");
        i2.setWeight(100);
        i2.setCode("I_2");

        Box loaded = service.loadItems(b.getId(), List.of(i1, i2));
        Assertions.assertEquals(2, loaded.getItems().size());
    }
}
