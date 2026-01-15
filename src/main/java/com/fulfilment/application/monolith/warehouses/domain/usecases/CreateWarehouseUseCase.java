package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.Constants;
import com.fulfilment.application.monolith.warehouses.Exception.WarehouseOperationFailedException;
import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.CreateWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@ApplicationScoped
public class CreateWarehouseUseCase implements CreateWarehouseOperation {

  private static final Logger logger = LoggerFactory.getLogger(CreateWarehouseUseCase.class);

  private final WarehouseStore warehouseStore;
  private final LocationResolver locationResolver;

  public CreateWarehouseUseCase(WarehouseStore warehouseStore,
                                LocationResolver locationResolver) {
    this.warehouseStore = warehouseStore;
    this.locationResolver = locationResolver;
  }

  @Override
  @Transactional
  public void create(Warehouse warehouse) {
    try {
      // **Business Unit Code Verification**
      List<Warehouse> existingWarehouse = warehouseStore.findByBusinessUnitCode(warehouse.getBusinessUnitCode());
      if (!existingWarehouse.isEmpty()) {
        logger.error(String.format(Constants.WAREHOUSE_ALREADY_EXISTS, warehouse.getBusinessUnitCode()));
        throw new WarehouseOperationFailedException(String.format(Constants.WAREHOUSE_ALREADY_EXISTS, warehouse.getBusinessUnitCode()));
      }

      //**Location Validation**
      Location location = locationResolver.resolveByIdentifier(warehouse.getLocation());
      if (locationResolver.resolveByIdentifier(warehouse.getLocation()) == null) {
        logger.error(String.format(Constants.NOT_VALID_LOCATION, warehouse.getLocation()));
        throw new WarehouseOperationFailedException(String.format(Constants.NOT_VALID_LOCATION, warehouse.getLocation()));
      }

      // **Warehouse Creation Feasibility**
      long existingCountOfWarehousesForLocation = warehouseStore.getNumberOfWarehousesForLocation(warehouse.getLocation());
      if (existingCountOfWarehousesForLocation >= location.getMaxNumberOfWarehouses()) {
        logger.error(String.format(Constants.WAREHOUSE_EXCEEDED_PER_LOCATION, warehouse.getLocation()));
        throw new WarehouseOperationFailedException(String.format(Constants.WAREHOUSE_EXCEEDED_PER_LOCATION, warehouse.getLocation()));
      }

      // **Capacity and **
      if (warehouse.getCapacity() > location.getMaxCapacity()) {
        logger.error(String.format(Constants.WAREHOUSE_CAPACITY_EXCEEDED_LOCATION_CAPACITY, warehouse.getCapacity(),
                location.getMaxCapacity()));
        throw new WarehouseOperationFailedException(String.format(Constants.WAREHOUSE_CAPACITY_EXCEEDED_LOCATION_CAPACITY, warehouse.getCapacity(),
                location.getMaxCapacity()));
      }

      // ** Stock Validation **
      if (warehouse.getStock() > warehouse.getCapacity()) {
        logger.error(String.format(Constants.WAREHOUSE_STOCK_EXCEEDED_WAREHOUSE_CAPACITY, warehouse.getStock()));
        throw new WarehouseOperationFailedException(String.format(Constants.WAREHOUSE_STOCK_EXCEEDED_WAREHOUSE_CAPACITY, warehouse.getStock()));
      }
      warehouseStore.create(warehouse);
      logger.info("New Warehouse Created Successfully");
    } catch(Exception e) {
      logger.error("Unable to create a new warehouse due to " + e.getMessage());
      throw e;
    }
  }
}
