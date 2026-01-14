package com.fulfilment.application.monolith.warehousefullfilment.domain.usecase;

import com.fulfilment.application.monolith.warehousefullfilment.Exception.WarehouseFullfilmentFailedException;
import com.fulfilment.application.monolith.warehousefullfilment.domain.model.WarehouseFullfilment;
import com.fulfilment.application.monolith.warehousefullfilment.domain.port.WarehouseFullfilmentStore;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
public class WarehouseAsFullfilmentUnitsUseCaseTest {
    @InjectMock
    WarehouseFullfilmentStore warehouseFullfilmentStore;

    @Inject
    WarehouseAsFullfilmentUnitsUseCase warehouseAsFullfilmentUnitsUseCase;

    @Test
    void shouldFailWhenWarehousePerProductPerStoreExceeds() {
        WarehouseFullfilment wf1 =
                new WarehouseFullfilment("MWH-001", "STORE-1", "PRODUCT-1", ZonedDateTime.now());

        when(warehouseFullfilmentStore.findNumberofWarehousesForAProductPerStore(wf1))
                .thenReturn(3L);

        WarehouseFullfilmentFailedException ex = assertThrows(WarehouseFullfilmentFailedException.class,
                () -> warehouseAsFullfilmentUnitsUseCase.createWarehouseFullfilment(wf1));

        assertTrue(ex.getMessage().contains("already assigned to 2 warehouses"));
        verify(warehouseFullfilmentStore, never()).create(any());
    }

}
