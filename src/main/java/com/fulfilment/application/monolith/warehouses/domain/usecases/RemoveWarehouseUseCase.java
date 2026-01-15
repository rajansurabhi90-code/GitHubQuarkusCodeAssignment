package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.Constants;
import com.fulfilment.application.monolith.warehouses.Exception.WarehouseNotFoundException;
import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;
import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.RemoveWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@ApplicationScoped
public class RemoveWarehouseUseCase implements RemoveWarehouseOperation {
    private static final Logger logger = LoggerFactory.getLogger(RemoveWarehouseUseCase.class);
    private final WarehouseStore warehouseStore;
    private final LocationResolver locationResolver;

    public RemoveWarehouseUseCase(WarehouseStore warehouseStore,
                                    LocationResolver locationResolver) {
        this.warehouseStore = warehouseStore;
        this.locationResolver = locationResolver;
    }

    @Override
    @Transactional
    public void removeById(Long id) {
        DbWarehouse warehouse = warehouseStore.findById(id.toString());
        if (warehouse == null) {
            logger.error("Active Warehouse with given business code does not Exists to replace");
            throw new WarehouseNotFoundException("Active Warehouse with given business code does "
                    + "not Exists to replace", 404);
        }
        if (locationResolver.resolveByIdentifier(warehouse.getLocation()) == null) {
            logger.error(String.format(Constants.NOT_VALID_LOCATION, warehouse.getLocation()));
            throw new RuntimeException(String.format(Constants.NOT_VALID_LOCATION, warehouse.getLocation()));
        }
        warehouseStore.remove(warehouse);
    }
}
