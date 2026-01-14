package com.fulfilment.application.monolith.warehouses.Exception;

public class WarehouseOperationFailedException extends RuntimeException{
    public WarehouseOperationFailedException(String message) {
        super(message);
    }
}
