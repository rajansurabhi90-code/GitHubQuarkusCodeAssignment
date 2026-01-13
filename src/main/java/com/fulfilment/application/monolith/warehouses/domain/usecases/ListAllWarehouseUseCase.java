package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.ListAllWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ListAllWarehouseUseCase implements ListAllWarehouseOperation {
    private final WarehouseStore warehouseStore;

    public ListAllWarehouseUseCase(WarehouseStore warehouseStore) {
        this.warehouseStore = warehouseStore;
    }
    @Override
    public List<Warehouse> getAllWarehouses() {
        return warehouseStore.findAllWarehouses();
    }
}
