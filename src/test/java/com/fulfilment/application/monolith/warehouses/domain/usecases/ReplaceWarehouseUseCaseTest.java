package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

@QuarkusTest
public class ReplaceWarehouseUseCaseTest {
    @InjectMock
    WarehouseStore warehouseStore;

    @InjectMock
    LocationResolver locationResolver;

    @Inject
    ReplaceWarehouseUseCase replaceWarehouseUseCase;

    @InjectMock
    WarehouseRepository warehouseRepository;

    @Test
    public void shouldFailWhenBusinessUnitDoesNotExists() {
        Warehouse warehouse = new Warehouse("MHW.000", "ZWOLLE-001", 10, 10, null, null);

        Mockito.when(warehouseStore.findByBusinessUnitCode("MHW.000"))
                .thenReturn(List.of());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> replaceWarehouseUseCase.replace(warehouse));

        assertTrue(ex.getMessage().contains("does not Exists to replace"));
        Mockito.verify(warehouseStore, Mockito.never()).create(Mockito.any());
    }

    @Test
    public void shouldFailWhenWarehouseCountExceedsLocationLimit() {
        Warehouse warehouse = new Warehouse("MHW.000", "ZWOLLE-001", 80, 10, null, null);
        Warehouse existingWarehouse = new Warehouse("MHW.000", "ZWOLLE-001", 10, 10, null,
                null);

        Mockito.when(warehouseStore.findByBusinessUnitCode("MHW.000"))
                .thenReturn(List.of(existingWarehouse));
        Mockito.when(warehouseStore.getActiveWarehouse(List.of(existingWarehouse)))
                        .thenReturn(existingWarehouse);

        Mockito.when(warehouseStore.getNumberOfWarehousesForLocation("ZWOLLE-001")).thenReturn(1L);

        Location location = new Location("ZWOLLE-001", 0, 0); // max 5 warehouses
        Mockito.when(locationResolver.resolveByIdentifier("ZWOLLE-001")).thenReturn(location);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> replaceWarehouseUseCase.replace(warehouse));

        assertTrue(ex.getMessage().contains("is already reached"));
        Mockito.verify(warehouseStore, Mockito.never()).create(Mockito.any());
    }

    @Test
    public void shouldFailWhenWarehouseCapacityExceedsLocationCapacity() {
        Warehouse warehouse = new Warehouse("MHW.000", "ZWOLLE-001", 100, 60, null, null); // 60 > maxCapacity
        Warehouse existingWarehouse = new Warehouse("MHW.000", "ZWOLLE-001", 10, 10, null, null);
        Mockito.when(warehouseStore.findByBusinessUnitCode("MHW.000"))
                .thenReturn(List.of(existingWarehouse));
        Mockito.when(warehouseStore.getActiveWarehouse(List.of(existingWarehouse)))
                .thenReturn(existingWarehouse);
        Mockito.when(warehouseStore.getNumberOfWarehousesForLocation("ZWOLLE-001")).thenReturn(0L);

        Location location = new Location("ZWOLLE-001", 5, 50); // maxCapacity 50
        Mockito.when(locationResolver.resolveByIdentifier("ZWOLLE-001")).thenReturn(location);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> replaceWarehouseUseCase.replace(warehouse));

        assertTrue(ex.getMessage().contains("cannot exceed"));
        Mockito.verify(warehouseStore, Mockito.never()).create(Mockito.any());
    }

    @Test
    public void shouldFailWhenWarehouseStockExceedsCapacity() {
        Warehouse warehouse = new Warehouse("MHW.000", "ZWOLLE-001", 50, 60, null, null); // stock > capacity
        Warehouse existingWarehouse = new Warehouse("MHW.000", "ZWOLLE-001", 90, 60, null, null);
        Mockito.when(warehouseStore.findByBusinessUnitCode("MHW.000")).thenReturn(List.of(existingWarehouse));
        Mockito.when(warehouseStore.getActiveWarehouse(List.of(existingWarehouse))).thenReturn(existingWarehouse);
        Mockito.when(warehouseStore.getNumberOfWarehousesForLocation("ZWOLLE-001")).thenReturn(1L);

        Location location = new Location("ZWOLLE-001", 5, 100); // maxCapacity 100
        Mockito.when(locationResolver.resolveByIdentifier("ZWOLLE-001")).thenReturn(location);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> replaceWarehouseUseCase.replace(warehouse));

        assertTrue(ex.getMessage().contains("cannot be more than"));
        Mockito.verify(warehouseStore, Mockito.never()).create(Mockito.any());

    }

    @Test
    public void shouldFailWhenReplacingWarehouseCapacityLessThanWarehouseStock() {
        Warehouse warehouse = new Warehouse("MHW.000", "ZWOLLE-001", 30, 20, null, null); // stock > capacity
        Warehouse existingWarehouse = new Warehouse("MHW.000", "ZWOLLE-001", 50, 40, null, null); // stock > capacity
        Mockito.when(warehouseStore.findByBusinessUnitCode("MHW.000")).thenReturn(List.of(existingWarehouse));
        Mockito.when(warehouseStore.getActiveWarehouse(List.of(existingWarehouse))).thenReturn(existingWarehouse);
        Mockito.when(warehouseStore.getNumberOfWarehousesForLocation("ZWOLLE-001")).thenReturn(1L);

        Location location = new Location("ZWOLLE-001", 5, 100); // maxCapacity 100
        Mockito.when(locationResolver.resolveByIdentifier("ZWOLLE-001")).thenReturn(location);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> replaceWarehouseUseCase.replace(warehouse));

        assertTrue(ex.getMessage().contains("cannot accomodate stock from"));
        Mockito.verify(warehouseStore, Mockito.never()).create(Mockito.any());

    }

    @Test
    public void shouldFailWhenReplacingWarehouseStockNotSameAsWarehouseStock() {
        Warehouse warehouse = new Warehouse("MHW.000", "ZWOLLE-001", 100, 50, null, null); // stock > capacity
        Warehouse existingWarehouse = new Warehouse("MHW.000", "ZWOLLE-001", 50, 40, null, null); // stock > capacity
        Mockito.when(warehouseStore.findByBusinessUnitCode("MHW.000")).thenReturn(List.of(existingWarehouse));
        Mockito.when(warehouseStore.getActiveWarehouse(List.of(existingWarehouse))).thenReturn(existingWarehouse);
        Mockito.when(warehouseStore.getNumberOfWarehousesForLocation("ZWOLLE-001")).thenReturn(1L);

        Location location = new Location("ZWOLLE-001", 5, 100); // maxCapacity 100
        Mockito.when(locationResolver.resolveByIdentifier("ZWOLLE-001")).thenReturn(location);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> replaceWarehouseUseCase.replace(warehouse));

        assertTrue(ex.getMessage().contains("being replaced is not same"));
        Mockito.verify(warehouseStore, Mockito.never()).create(Mockito.any());

    }


    @Test
    public void shouldReplaceWarehouseSuccessfully() {
        Warehouse warehouse = new Warehouse("MHW.000", "ZWOLLE-001", 90, 60, null, null); // stock > capacity
        Warehouse existingWarehouse = new Warehouse("MHW.000", "ZWOLLE-001", 70, 60, null, null); // stock > capacity
        Mockito.when(warehouseStore.findByBusinessUnitCode("MHW.000")).thenReturn(List.of(existingWarehouse));
        Mockito.when(warehouseStore.getActiveWarehouse(List.of(existingWarehouse))).thenReturn(existingWarehouse);
        Mockito.when(warehouseStore.getNumberOfWarehousesForLocation("ZWOLLE-001")).thenReturn(1L);

        Location location = new Location("ZWOLLE-001", 5, 100); // maxCapacity 100
        Mockito.when(locationResolver.resolveByIdentifier("ZWOLLE-001")).thenReturn(location);

        replaceWarehouseUseCase.replace(warehouse);
        verify(warehouseStore).update(existingWarehouse); // the old one
        verify(warehouseStore).create(warehouse);

    }
}
