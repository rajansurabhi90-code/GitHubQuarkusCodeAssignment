package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
public class ArchiveWarehouseUnitByIdUseCaseTest {
    @InjectMock
    WarehouseStore warehouseStore;

    @Inject
    ArchiveWarehouseUnitByIdUseCase archiveWarehouseUnitByIdUseCase;

    @Test
    public void shouldFailWhenWarehouseByIdNull() {
        when(warehouseStore.findById("1")).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> archiveWarehouseUnitByIdUseCase.archiveWareHouseUnitById("1"));
        assertTrue(ex.getMessage().contains("Warehouse unit not found "));
    }
}
