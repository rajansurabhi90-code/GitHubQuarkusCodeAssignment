package com.fulfilment.application.monolith.warehouses.adapters.restapi.database;

import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;
import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class WarehouseRepositoryTest {

    @Inject
    WarehouseRepository repository;

    @Inject
    EntityManager em;

    @BeforeEach
    @Transactional
    void cleanDb() {
        repository.deleteAll();
    }

    @Test
    @Transactional
    void testCreateWarehouse() {
        Warehouse warehouse = new Warehouse("BU1", "Paris", 1000, 500,
                ZonedDateTime.now(), null);

        repository.create(warehouse);

        List<Warehouse> all = repository.findAllWarehouses();
        assertEquals(1, all.size());
        assertEquals("BU1", all.get(0).getBusinessUnitCode());
    }

    @Test
    @Transactional
    void testUpdateInsertsNewRow() {
        Warehouse warehouse = new Warehouse("BU2", "Berlin", 2000, 800,
                ZonedDateTime.now(), null);
        repository.create(warehouse);
        repository.update(warehouse);

        List<Warehouse> all = repository.findAllWarehouses();
        assertEquals(2, all.size());

        Warehouse original = all.stream()
                .filter(w -> w.getArchivedAt() == null)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Original warehouse not found"));

        Warehouse updated = all.stream()
                .filter(w -> w.getArchivedAt() != null)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Updated warehouse not found"));

        assertEquals("Berlin", original.getLocation());
        assertEquals("Berlin", updated.getLocation());
        assertNotNull(updated.getArchivedAt());
    }

    @Test
    void testRemoveThrows() {
        Warehouse warehouse = new Warehouse("BU3", "London", 1000, 500,
                ZonedDateTime.now(), null);
        UnsupportedOperationException ex = assertThrows(
                UnsupportedOperationException.class,
                () -> repository.remove(warehouse)
        );
        assertEquals("Unimplemented method 'remove'", ex.getMessage());
    }

    @Test
    @Transactional
    void testFindByBusinessUnitCode() {
        repository.create(new Warehouse("BU4", "Rome", 500, 200,
                ZonedDateTime.now(), null));
        repository.create(new Warehouse("BU4", "Naples", 600, 300,
                ZonedDateTime.now(), null));

        List<Warehouse> list = repository.findByBusinessUnitCode("BU4");
        assertEquals(2, list.size());

        List<Warehouse> empty = repository.findByBusinessUnitCode("UNKNOWN");
        assertTrue(empty.isEmpty());
    }

    @Test
    void testGetActiveWarehouse() {
        Warehouse active = new Warehouse("BU5", "Vienna", 700, 400,
                ZonedDateTime.now(), null);
        Warehouse archived = new Warehouse("BU5", "Salzburg", 700, 400,
                ZonedDateTime.now(), ZonedDateTime.now());

        Warehouse result = repository.getActiveWarehouse(List.of(active, archived));
        assertEquals("Vienna", result.getLocation());

        assertThrows(RuntimeException.class,
                () -> repository.getActiveWarehouse(List.of(archived)));
    }

    @Test
    @Transactional
    void testGetNumberOfWarehousesForLocation() {
        repository.create(new Warehouse("BU6", "Madrid", 500, 200, ZonedDateTime.now(), null));
        repository.create(new Warehouse("BU7", "Madrid", 600, 300, ZonedDateTime.now(), ZonedDateTime.now()));

        long count = repository.getNumberOfWarehousesForLocation("Madrid");
        assertEquals(1, count);
    }

    @Test
    @Transactional
    void testFindById() {
        DbWarehouse dbWarehouse = new DbWarehouse();
        dbWarehouse.setBusinessUnitCode("BU8");
        dbWarehouse.setLocation("Lisbon");
        dbWarehouse.setCapacity(500);
        dbWarehouse.setStock(200);
        dbWarehouse.setCreatedAt(LocalDateTime.now());

        em.persist(dbWarehouse);
        em.flush();

        Warehouse found = repository.findById(dbWarehouse.getId().toString());
        assertNotNull(found);
        assertEquals("Lisbon", found.getLocation());
    }

    @Test
    @Transactional
    void testFindAllWarehouses() {
        repository.create(new Warehouse("BU9", "Oslo", 500, 200, ZonedDateTime.now(), null));
        repository.create(new Warehouse("BU10", "Stockholm", 600, 300, ZonedDateTime.now(), null));

        List<Warehouse> all = repository.findAllWarehouses();
        assertEquals(2, all.size());
    }
}
