package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
public class RemoveWarehouseUnitCaseTest {
    @InjectMock
    WarehouseStore warehouseStore;

    @InjectMock
    LocationResolver locationResolver;

    @Inject
    RemoveWarehouseUseCase removeWarehouseUseCase;

    @InjectMock
    WarehouseRepository warehouseRepository;

    @Test
    void shouldFailWhenBusinessUnitDoesNotExists() {
        Warehouse warehouse = new Warehouse("MHW.000", "ZWOLLE-001", 10, 10, null, null);

        when(warehouseStore.findByBusinessUnitCode("MHW.000"))
                .thenReturn(List.of());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> removeWarehouseUseCase.remove(warehouse));

        assertTrue(ex.getMessage().contains("does not Exists to replace"));
        verify(warehouseStore, never()).create(any());
    }

    void shouldFailWhenLocationInvalid() {
        Warehouse warehouse = new Warehouse("MHW.000", "INVALID_LOC", 10, 10, null, null);

        when(warehouseStore.findByBusinessUnitCode("MHW.000")).thenReturn(List.of());
        when(locationResolver.resolveByIdentifier("INVALID_LOC")).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> removeWarehouseUseCase.remove(warehouse));

        assertTrue(ex.getMessage().contains("Doesnt Exist"));
        verify(warehouseStore, never()).create(any());
    }
}
