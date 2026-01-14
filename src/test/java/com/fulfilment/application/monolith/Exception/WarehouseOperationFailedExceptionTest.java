package com.fulfilment.application.monolith.Exception;

import com.fulfilment.application.monolith.warehouses.Exception.WarehouseOperationFailedException;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class WarehouseOperationFailedExceptionTest {
    @Test
    void testExceptionMessage() {
        String message = "Warehouse Use case operation failed";

        WarehouseOperationFailedException exception =
                new WarehouseOperationFailedException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void testExceptionIsRuntimeException() {
        WarehouseOperationFailedException exception =
                new WarehouseOperationFailedException("Error");

        assertTrue(exception instanceof RuntimeException);
    }
}
