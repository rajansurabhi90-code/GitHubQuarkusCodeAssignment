package com.fulfilment.application.monolith.warehouses.domain.usecases.models;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class WarehouseTest {
    @Test
    public void testConstructorAndGetters() {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);

        Warehouse warehouse = new Warehouse(
                "BU001",
                "LOC001",
                100,
                50,
                now,
                null
        );

        assertEquals("BU001", warehouse.getBusinessUnitCode());
        assertEquals("LOC001", warehouse.getLocation());
        assertEquals(100, warehouse.getCapacity());
        assertEquals(50, warehouse.getStock());
        assertEquals(now, warehouse.getCreationAt());
        assertNull(warehouse.getArchivedAt());
    }

    @Test
    public void testSetters() {
        Warehouse warehouse = new Warehouse(
                "BU001",
                "LOC001",
                100,
                50,
                null,
                null
        );

        ZonedDateTime creation = ZonedDateTime.now(ZoneOffset.UTC);
        ZonedDateTime archived = ZonedDateTime.now(ZoneOffset.UTC).plusDays(1);

        warehouse.setBusinessUnitCode("BU002");
        warehouse.setLocation("LOC002");
        warehouse.setCapacity(200);
        warehouse.setStock(75);
        warehouse.setCreationAt(creation);
        warehouse.setArchivedAt(archived);

        assertEquals("BU002", warehouse.getBusinessUnitCode());
        assertEquals("LOC002", warehouse.getLocation());
        assertEquals(200, warehouse.getCapacity());
        assertEquals(75, warehouse.getStock());
        assertEquals(creation, warehouse.getCreationAt());
        assertEquals(archived, warehouse.getArchivedAt());
    }
    @Test
    void testSettersAndGetters() {
        Warehouse warehouse = new Warehouse();


        warehouse.setLocation("LOC-1");
        warehouse.setCapacity(100);
        warehouse.setStock(50);


        assertEquals("LOC-1", warehouse.getLocation());
        assertEquals(100, warehouse.getCapacity());
        assertEquals(50, warehouse.getStock());
    }

    @Test
    void testConstructorWithArguments() {
        Warehouse warehouse = new Warehouse("WH-002", "LOC-2", 200, 150, null,null);

        assertEquals("LOC-2", warehouse.getLocation());
        assertEquals(200, warehouse.getCapacity());
        assertEquals(150, warehouse.getStock());
    }

    @Test
    void testDefaultConstructor() {
        Warehouse warehouse = new Warehouse();
        assertNotNull(warehouse);
    }
}
