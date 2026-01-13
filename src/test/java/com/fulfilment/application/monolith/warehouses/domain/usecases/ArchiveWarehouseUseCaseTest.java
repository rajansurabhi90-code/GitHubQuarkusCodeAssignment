package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
public class ArchiveWarehouseUseCaseTest {
    @InjectMock
    WarehouseStore warehouseStore;

    @InjectMock
    LocationResolver locationResolver;

    @Inject
    ArchiveWarehouseUseCase archiveWarehouseUseCase;

    @Test
    public void shouldFailWhenBusinessUnitDoesNotExists() {
        Warehouse warehouse = new Warehouse("MHW.000", "ZWOLLE-001", 10, 10, null, null);

        when(warehouseStore.findByBusinessUnitCode("MHW.000"))
                .thenReturn(List.of());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> archiveWarehouseUseCase.archive(warehouse));

        assertTrue(ex.getMessage().contains("does not Exists to archive"));
        Mockito.verify(warehouseStore, Mockito.never()).create(Mockito.any());
    }

    @Test
    public void testWarehouseArchiveSucess() {
        Warehouse warehouse = new Warehouse("MHW.000", "ZWOLLE-001", 10, 10, null, null);
        List<Warehouse> existingWarehouses = new ArrayList<>();
        Warehouse activeWarehouse =
                new Warehouse("MHW.000", "ZWOLLE-001", 10, 10, null, null);
        Warehouse archivedWarehouse =
                new Warehouse("MHW.000", "ZWOLLE-001", 8, 5, null, ZonedDateTime.now());

        existingWarehouses.add(activeWarehouse);
        existingWarehouses.add(archivedWarehouse);

        when(warehouseStore.findByBusinessUnitCode("MHW.000"))
                .thenReturn(existingWarehouses);
        when(warehouseStore.getActiveWarehouse(existingWarehouses))
                        .thenReturn(activeWarehouse);

        archiveWarehouseUseCase.archive(activeWarehouse);
        verify(warehouseStore).update(activeWarehouse);
    }
}
