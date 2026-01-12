package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.Exception.WarehouseNotFoundException;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.GetWarehouseByIdOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GetWarehouseUnitByIdUseCase implements GetWarehouseByIdOperation {
    private final WarehouseStore warehouseStore;

    public GetWarehouseUnitByIdUseCase(WarehouseStore warehouseStore) {
        this.warehouseStore = warehouseStore;
    }

    @Override
    public Warehouse getWarehouseUnitById(String id) {
        Warehouse warehouse = warehouseStore.findById(id);
        if (warehouse == null) {
            throw new WarehouseNotFoundException("No warehouse unit found for given id " + id, 404);
        }
        return warehouse;
    }

}
