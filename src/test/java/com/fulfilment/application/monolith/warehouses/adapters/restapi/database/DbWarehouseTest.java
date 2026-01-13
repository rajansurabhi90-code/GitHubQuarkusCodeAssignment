package com.fulfilment.application.monolith.warehouses.adapters.restapi.database;

import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class DbWarehouseTest {
    @Test
    void testGetterAndSetter() {
        DbWarehouse warehouse = new DbWarehouse();

        LocalDateTime now = LocalDateTime.now();

        warehouse.setId(1L);
        warehouse.setBusinessUnitCode("MWH.004");
        warehouse.setLocation("New York");
        warehouse.setCapacity(1000);
        warehouse.setStock(500);
        warehouse.setCreatedAt(now);
        warehouse.setArchivedAt(now.plusDays(1));

        assertEquals(1L, warehouse.getId());
        assertEquals("MWH.004", warehouse.getBusinessUnitCode());
        assertEquals("New York", warehouse.getLocation());
        assertEquals(1000, warehouse.getCapacity());
        assertEquals(500, warehouse.getStock());
        assertEquals(now, warehouse.getCreatedAt());
        assertEquals(now.plusDays(1), warehouse.getArchivedAt());
    }
}
