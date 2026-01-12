package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.Exception.WarehouseNotFoundException;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.ArchiveWarehouseUnitByIdOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ArchiveWarehouseUnitByIdUseCase implements ArchiveWarehouseUnitByIdOperation {
    private final WarehouseStore warehouseStore;

    public ArchiveWarehouseUnitByIdUseCase(WarehouseStore warehouseStore) {
        this.warehouseStore = warehouseStore;
    }
    @Override
    public void archiveWareHouseUnitById(String id) {
        Warehouse warehouse = warehouseStore.findById(id);
        if (warehouse == null) {
            throw new WarehouseNotFoundException("Warehouse unit not found " + id , 404);
        }
        warehouseStore.update(warehouse);
    }
}
