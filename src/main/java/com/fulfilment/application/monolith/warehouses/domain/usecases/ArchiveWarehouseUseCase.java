package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.Exception.WarehouseNotFoundException;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.ArchiveWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ArchiveWarehouseUseCase implements ArchiveWarehouseOperation {

  private final WarehouseStore warehouseStore;

  public ArchiveWarehouseUseCase(WarehouseStore warehouseStore) {
    this.warehouseStore = warehouseStore;
  }

  @Override
  public void archive(Warehouse warehouse) {
    List<Warehouse> existingWarehouseList = warehouseStore.findByBusinessUnitCode(warehouse.getBusinessUnitCode());
    Warehouse existingActiveWarehouse =  warehouseStore.getActiveWarehouse(existingWarehouseList);
    if (existingActiveWarehouse == null) {
      throw new WarehouseNotFoundException("Warehouse with given business code "
              + warehouse.getBusinessUnitCode() + "does not Exists to archive", 404);
    }
    Warehouse activeWarehouseToBeArchived = warehouseStore.getActiveWarehouse(existingWarehouseList);
    warehouseStore.update(activeWarehouseToBeArchived);
  }
}
