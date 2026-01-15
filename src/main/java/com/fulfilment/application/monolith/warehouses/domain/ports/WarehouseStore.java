package com.fulfilment.application.monolith.warehouses.domain.ports;

import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;

import java.util.List;

public interface WarehouseStore {
  void create(Warehouse warehouse);

  void update(Warehouse warehouse);

  List<Warehouse> findByBusinessUnitCode(String buCode);

  Warehouse getActiveWarehouse(List<Warehouse> warehouses);

  long getNumberOfWarehousesForLocation(String location);
  DbWarehouse findById(String id);
  List<Warehouse> findAllWarehouses();
  void remove(DbWarehouse warehouse);
  Warehouse warehouseFromEntity(DbWarehouse dbWarehouse);
  DbWarehouse entityFromWareHouse(Warehouse warehouse);
}
