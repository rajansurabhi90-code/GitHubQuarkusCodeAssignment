package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@QuarkusTest
public class GetWarehouseByIdUseCaseTest {
    @InjectMock
    WarehouseStore warehouseStore;

    @Inject
    GetWarehouseUnitByIdUseCase getWarehouseUnitByIdUseCase;

    @Test
    public void shouldFailWhenWarehouseByIdNull() {
        when(warehouseStore.findById("1")).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> getWarehouseUnitByIdUseCase.getWarehouseUnitById("1"));
        assertTrue(ex.getMessage().contains("No warehouse unit found "));
    }
}
