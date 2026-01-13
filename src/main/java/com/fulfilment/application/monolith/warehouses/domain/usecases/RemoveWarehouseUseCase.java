package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.Exception.WarehouseNotFoundException;
import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.RemoveWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class RemoveWarehouseUseCase implements RemoveWarehouseOperation {
    private final WarehouseStore warehouseStore;
    private final LocationResolver locationResolver;

    public RemoveWarehouseUseCase(WarehouseStore warehouseStore,
                                    LocationResolver locationResolver) {
        this.warehouseStore = warehouseStore;
        this.locationResolver = locationResolver;
    }

    @Override
    public void remove(Warehouse warehouse) {
        List<Warehouse> existingWarehouseList = warehouseStore.findByBusinessUnitCode(warehouse.getBusinessUnitCode());
        if (existingWarehouseList.isEmpty()) {
            throw new WarehouseNotFoundException("Active Warehouse with given business code does "
                    + "not Exists to replace", 404);
        }
        Location location = locationResolver.resolveByIdentifier(warehouse.getLocation());
        if (locationResolver.resolveByIdentifier(warehouse.getLocation()) == null) {
            throw new RuntimeException("Invalid Location " + warehouse.getLocation() + " Doesnt Exist");
        }
        warehouseStore.remove(warehouse);
    }
}
