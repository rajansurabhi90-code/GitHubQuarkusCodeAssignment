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
public class
CreateWarehouseUseCaseTest {
    @InjectMock
    WarehouseStore warehouseStore;

    @InjectMock
    LocationResolver locationResolver;

    @Inject
    CreateWarehouseUseCase createWarehouseUseCase;

    @InjectMock
    WarehouseRepository warehouseRepository;

    @Test
    public void shouldFailWhenBusinessUnitAlreadyExists() {
        Warehouse warehouse = new Warehouse("MHW.000", "ZWOLLE-001", 10, 10, null, null);

        Mockito.when(warehouseStore.findByBusinessUnitCode("MHW.000"))
                .thenReturn(List.of(new Warehouse("MHW.000", "ZWOLLE-001", 10, 10, null, null)));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> createWarehouseUseCase.create(warehouse));

        assertTrue(ex.getMessage().contains("already Exists"));
        Mockito.verify(warehouseStore, Mockito.never()).create(Mockito.any());
    }

    @Test
    public void shouldFailWhenLocationInvalid() {
        Warehouse warehouse = new Warehouse("MHW.000", "INVALID_LOC", 10, 10, null, null);

        Mockito.when(warehouseStore.findByBusinessUnitCode("MHW.000")).thenReturn(List.of());
        Mockito.when(locationResolver.resolveByIdentifier("INVALID_LOC")).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> createWarehouseUseCase.create(warehouse));

        assertTrue(ex.getMessage().contains("Doesnt Exist"));
        Mockito.verify(warehouseStore, Mockito.never()).create(Mockito.any());
    }

    @Test
    public void shouldFailWhenWarehouseCountExceedsLocationLimit() {
        Warehouse warehouse = new Warehouse("MHW.000", "ZWOLLE-001", 10, 10, null, null);

        Mockito.when(warehouseStore.findByBusinessUnitCode("MHW.000")).thenReturn(List.of());
        Mockito.when(warehouseStore.getNumberOfWarehousesForLocation("ZWOLLE-001")).thenReturn(5L);

        Location location = new Location("ZWOLLE-001", 5, 50); // max 5 warehouses
        Mockito.when(locationResolver.resolveByIdentifier("ZWOLLE-001")).thenReturn(location);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> createWarehouseUseCase.create(warehouse));

        assertTrue(ex.getMessage().contains("already reached"));
        Mockito.verify(warehouseStore, Mockito.never()).create(Mockito.any());
    }

    @Test
    public void shouldFailWhenWarehouseCapacityExceedsLocationCapacity() {
        Warehouse warehouse = new Warehouse("MHW.000", "ZWOLLE-001", 100, 60, null, null); // 60 > maxCapacity

        Mockito.when(warehouseStore.findByBusinessUnitCode("MHW.000")).thenReturn(List.of());
        Mockito.when(warehouseStore.getNumberOfWarehousesForLocation("ZWOLLE-001")).thenReturn(0L);

        Location location = new Location("ZWOLLE-001", 5, 50); // maxCapacity 50
        Mockito.when(locationResolver.resolveByIdentifier("ZWOLLE-001")).thenReturn(location);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> createWarehouseUseCase.create(warehouse));

        assertTrue(ex.getMessage().contains("cannot exceed"));
        Mockito.verify(warehouseStore, Mockito.never()).create(Mockito.any());
    }

    @Test
    public void shouldFailWhenWarehouseStockExceedsCapacity() {
        Warehouse warehouse = new Warehouse("MHW.000", "ZWOLLE-001", 50, 60, null, null); // stock > capacity

        Mockito.when(warehouseStore.findByBusinessUnitCode("MHW.000")).thenReturn(List.of());
        Mockito.when(warehouseStore.getNumberOfWarehousesForLocation("ZWOLLE-001")).thenReturn(0L);

        Location location = new Location("ZWOLLE-001", 5, 100); // maxCapacity 100
        Mockito.when(locationResolver.resolveByIdentifier("ZWOLLE-001")).thenReturn(location);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> createWarehouseUseCase.create(warehouse));

        assertTrue(ex.getMessage().contains("cannot be more than"));
        Mockito.verify(warehouseStore, Mockito.never()).create(Mockito.any());

    }

    @Test
    public void shouldCreateWarehouseSuccessfully() {
        Warehouse warehouse = new Warehouse("MHW.000", "ZWOLLE-001", 90, 60, null, null); // stock > capacity

        Mockito.when(warehouseStore.findByBusinessUnitCode("MHW.000")).thenReturn(List.of());
        Mockito.when(warehouseStore.getNumberOfWarehousesForLocation("ZWOLLE-001")).thenReturn(1L);

        Location location = new Location("ZWOLLE-001", 5, 100); // maxCapacity 100
        Mockito.when(locationResolver.resolveByIdentifier("ZWOLLE-001")).thenReturn(location);

        createWarehouseUseCase.create(warehouse);
        verify(warehouseStore).create(warehouse);

    }

}
