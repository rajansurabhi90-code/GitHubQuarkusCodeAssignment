package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.Exception.WarehouseNotFoundException;
import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.ReplaceWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class ReplaceWarehouseUseCase implements ReplaceWarehouseOperation {

  private final WarehouseStore warehouseStore;
  private final LocationResolver locationResolver;

  public ReplaceWarehouseUseCase(WarehouseStore warehouseStore,
                                 LocationResolver locationResolver) {
    this.warehouseStore = warehouseStore;
    this.locationResolver = locationResolver;
  }

  @Override
  public void replace(Warehouse newWarehouse) {
    // **Business Unit Code Verification**
    List<Warehouse> existingWarehouseList = warehouseStore.findByBusinessUnitCode(newWarehouse.getBusinessUnitCode());
    Warehouse existingActiveWarehouse =  warehouseStore.getActiveWarehouse(existingWarehouseList);
    if (existingActiveWarehouse == null) {
      throw new WarehouseNotFoundException("Active Warehouse with given business code "
              + newWarehouse.getBusinessUnitCode() + " does not Exists to replace", 404);
    }

    //**Location Validation**
    Location location = locationResolver.resolveByIdentifier(newWarehouse.getLocation());
    if (locationResolver.resolveByIdentifier(newWarehouse.getLocation()) == null) {
      throw new RuntimeException("Invalid Location " + newWarehouse.getLocation() + " Doesnt Exist");
    }

    // **Warehouse Creation Feasibility**
    long existingCountOfWarehousesForLocation = warehouseStore.getNumberOfWarehousesForLocation(newWarehouse.getLocation());
    if (existingCountOfWarehousesForLocation > location.getMaxNumberOfWarehouses()) {
      throw new RuntimeException("Number For Warehouses For A Given Location "
              + newWarehouse.getLocation() + " is already reached");
    }

    // **Capacity and **
    if (newWarehouse.getCapacity() > location.getMaxCapacity()) {
      throw new RuntimeException("Warehouse capacity " + newWarehouse.getCapacity() + " cannot exceed the "
              + " maxCapacity" + location.getMaxCapacity() + " of a location");
    }

    // ** Stock Validation **
    if (newWarehouse.getStock() > newWarehouse.getCapacity()) {
      throw new RuntimeException("Warehouse stock " + newWarehouse.getStock() + " cannot be more than"
              + " warehouse capacity");
    }

    // **Capacity Accommodation**
    if (newWarehouse.getCapacity() < existingActiveWarehouse.getStock()) {
      throw new RuntimeException("Capacity of the stock being replaced cannot accomodate stock from " +
              "previous warehouse");
    }
    // **Stock Matching**
    if (!Objects.equals(newWarehouse.getStock(), existingActiveWarehouse.getStock())) {
      throw new RuntimeException("Stock from warehouse being replaced is not same.");
    }
    // arhive old warehouse
    warehouseStore.update(existingActiveWarehouse);

    // replace newWarehouse
    warehouseStore.create(newWarehouse);
  }
}
