package com.fulfilment.application.monolith.warehouses.Exception;

public class WarehouseNotFoundException extends RuntimeException{
    private final int statusCode;
    public WarehouseNotFoundException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
