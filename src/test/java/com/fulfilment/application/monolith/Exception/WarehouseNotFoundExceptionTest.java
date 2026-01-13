package com.fulfilment.application.monolith.Exception;

import com.fulfilment.application.monolith.warehouses.Exception.WarehouseNotFoundException;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class WarehouseNotFoundExceptionTest {
    @Test
    void testExceptionMessageAndStatusCode() {
        String message = "Warehouse not found";
        int statusCode = 404;

        WarehouseNotFoundException exception =
                new WarehouseNotFoundException(message, statusCode);

        assertEquals(message, exception.getMessage());
        assertEquals(statusCode, exception.getStatusCode());
    }

    @Test
    void testExceptionIsRuntimeException() {
        WarehouseNotFoundException exception =
                new WarehouseNotFoundException("Error", 404);

        assertTrue(exception instanceof RuntimeException);
    }
}
