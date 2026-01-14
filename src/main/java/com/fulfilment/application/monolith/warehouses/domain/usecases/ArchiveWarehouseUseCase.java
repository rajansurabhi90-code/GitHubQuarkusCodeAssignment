package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.Constants;
import com.fulfilment.application.monolith.warehouses.Exception.WarehouseNotFoundException;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.ArchiveWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@ApplicationScoped
public class ArchiveWarehouseUseCase implements ArchiveWarehouseOperation {
  private static final Logger logger = LoggerFactory.getLogger(ArchiveWarehouseUseCase.class);

  private final WarehouseStore warehouseStore;

  public ArchiveWarehouseUseCase(WarehouseStore warehouseStore) {
    this.warehouseStore = warehouseStore;
  }

  @Override
  public void archive(Warehouse warehouse) {
    try {
      List<Warehouse> existingWarehouseList = warehouseStore.findByBusinessUnitCode(warehouse.getBusinessUnitCode());
      Warehouse existingActiveWarehouse = warehouseStore.getActiveWarehouse(existingWarehouseList);
      if (existingActiveWarehouse == null) {
        logger.error(String.format(Constants.WAREHOUSE_DOES_NOT_EXISTS, warehouse.getBusinessUnitCode()));
        throw new WarehouseNotFoundException(String.format(Constants.WAREHOUSE_DOES_NOT_EXISTS, warehouse.getBusinessUnitCode()),
                404);
      }
      Warehouse activeWarehouseToBeArchived = warehouseStore.getActiveWarehouse(existingWarehouseList);
      warehouseStore.update(activeWarehouseToBeArchived);
      logger.info("Archived Successfully");
    } catch(Exception e) {
      logger.error("Unable to archive a warehouse due to " + e.getMessage());
      throw e;
    }
  }
}
