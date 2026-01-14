package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.Constants;
import com.fulfilment.application.monolith.warehouses.Exception.WarehouseNotFoundException;
import com.fulfilment.application.monolith.warehouses.Exception.WarehouseOperationFailedException;
import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.ReplaceWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class ReplaceWarehouseUseCase implements ReplaceWarehouseOperation {
  private static final Logger logger = LoggerFactory.getLogger(ReplaceWarehouseUseCase.class);

  private final WarehouseStore warehouseStore;
  private final LocationResolver locationResolver;

  public ReplaceWarehouseUseCase(WarehouseStore warehouseStore,
                                 LocationResolver locationResolver) {
    this.warehouseStore = warehouseStore;
    this.locationResolver = locationResolver;
  }

  @Override
  public void replace(Warehouse newWarehouse) {
    try {
      // **Business Unit Code Verification**
      List<Warehouse> existingWarehouseList = warehouseStore.findByBusinessUnitCode(newWarehouse.getBusinessUnitCode());
      Warehouse existingActiveWarehouse = warehouseStore.getActiveWarehouse(existingWarehouseList);
      if (existingActiveWarehouse == null) {
        logger.error(String.format(Constants.ACTIVE_WAREHOUSE_NOT_FOUND, newWarehouse.getBusinessUnitCode()));
        throw new WarehouseNotFoundException(String.format(Constants.ACTIVE_WAREHOUSE_NOT_FOUND, newWarehouse.getBusinessUnitCode()),
                404);
      }

      //**Location Validation**
      Location location = locationResolver.resolveByIdentifier(newWarehouse.getLocation());
      if (locationResolver.resolveByIdentifier(newWarehouse.getLocation()) == null) {
        logger.error(String.format(Constants.NOT_VALID_LOCATION, newWarehouse.getLocation()));
        throw new WarehouseOperationFailedException(String.format(Constants.NOT_VALID_LOCATION, newWarehouse.getLocation()));
      }

      // **Warehouse Creation Feasibility**
      long existingCountOfWarehousesForLocation = warehouseStore.getNumberOfWarehousesForLocation(newWarehouse.getLocation());
      if (existingCountOfWarehousesForLocation > location.getMaxNumberOfWarehouses()) {
        logger.error(String.format(Constants.WAREHOUSE_EXCEEDED_PER_LOCATION, newWarehouse.getLocation()));
        throw new WarehouseOperationFailedException(String.format(Constants.WAREHOUSE_EXCEEDED_PER_LOCATION,
                newWarehouse.getLocation()));
      }

      // **Capacity and **
      if (newWarehouse.getCapacity() > location.getMaxCapacity()) {
        logger.error(String.format(Constants.WAREHOUSE_CAPACITY_EXCEEDED_LOCATION_CAPACITY, newWarehouse.getCapacity(),
                location.getMaxCapacity()));
        throw new WarehouseOperationFailedException(String.format(Constants.WAREHOUSE_CAPACITY_EXCEEDED_LOCATION_CAPACITY, newWarehouse.getCapacity(),
                location.getMaxCapacity()));
      }

      // ** Stock Validation **
      if (newWarehouse.getStock() > newWarehouse.getCapacity()) {
        logger.error(String.format(Constants.WAREHOUSE_STOCK_EXCEEDED_WAREHOUSE_CAPACITY, newWarehouse.getStock()));
        throw new WarehouseOperationFailedException(String.format(Constants.WAREHOUSE_STOCK_EXCEEDED_WAREHOUSE_CAPACITY, newWarehouse.getStock()));
      }

      // **Capacity Accommodation**
      if (newWarehouse.getCapacity() < existingActiveWarehouse.getStock()) {
        logger.error(Constants.NEWWAREHOUSE_CAPACITY_NOT_ENOUGH);
        throw new WarehouseOperationFailedException(Constants.NEWWAREHOUSE_CAPACITY_NOT_ENOUGH);
      }
      // **Stock Matching**
      if (!Objects.equals(newWarehouse.getStock(), existingActiveWarehouse.getStock())) {
        logger.error(Constants.STOCKS_ARENT_SAME);
        throw new WarehouseOperationFailedException(Constants.STOCKS_ARENT_SAME);
      }
      // arhive old warehouse
      warehouseStore.update(existingActiveWarehouse);
      logger.info("old warehouse arhived successfully");
      // replace newWarehouse
      warehouseStore.create(newWarehouse);
      logger.info("new warehouse replaced successfully");
    } catch(Exception e) {
      logger.error("Unable to replace a new warehouse due to " + e.getMessage());
      throw e;
    }
  }
}
