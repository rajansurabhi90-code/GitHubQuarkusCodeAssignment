package com.fulfilment.application.monolith.Exception;

import com.fulfilment.application.monolith.warehousefullfilment.Exception.WarehouseFullfilmentFailedException;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class WarehouseFullfilmentFailedExceptionTest {
    @Test
    void testExceptionMessage() {
        String message = "WarehouseFullfilment failed";

        WarehouseFullfilmentFailedException exception =
                new WarehouseFullfilmentFailedException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void testExceptionIsRuntimeException() {
        WarehouseFullfilmentFailedException exception =
                new WarehouseFullfilmentFailedException("Error");

        assertTrue(exception instanceof RuntimeException);
    }
}
