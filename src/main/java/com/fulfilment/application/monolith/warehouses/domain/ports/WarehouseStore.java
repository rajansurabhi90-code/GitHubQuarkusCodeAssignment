package com.fulfilment.application.monolith.warehouses.domain.ports;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;

import java.util.List;

public interface WarehouseStore {
  void create(Warehouse warehouse);

  void update(Warehouse warehouse);

  void remove(Warehouse warehouse);

  List<Warehouse> findByBusinessUnitCode(String buCode);

  Warehouse getActiveWarehouse(List<Warehouse> warehouses);

  long getNumberOfWarehousesForLocation(String location);
  Warehouse findById(String id);
}
