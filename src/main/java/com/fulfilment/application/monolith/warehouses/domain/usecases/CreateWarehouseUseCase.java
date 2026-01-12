package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.CreateWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class CreateWarehouseUseCase implements CreateWarehouseOperation {

  private final WarehouseStore warehouseStore;
  private final LocationResolver locationResolver;

  public CreateWarehouseUseCase(WarehouseStore warehouseStore,
                                LocationResolver locationResolver) {
    this.warehouseStore = warehouseStore;
    this.locationResolver = locationResolver;
  }

  @Override
  public void create(Warehouse warehouse) {
    // **Business Unit Code Verification**
    List<Warehouse> existingWarehouse = warehouseStore.findByBusinessUnitCode(warehouse.getBusinessUnitCode());
    if (!existingWarehouse.isEmpty()) {
      throw new RuntimeException("Warehouses with given business code "
              + warehouse.getBusinessUnitCode() + "already Exists");
    }

    //**Location Validation**
    Location location = locationResolver.resolveByIdentifier(warehouse.getLocation());
    if (locationResolver.resolveByIdentifier(warehouse.getLocation()) == null) {
      throw new RuntimeException("Invalid Location " + warehouse.getLocation() + " Doesnt Exist");
    }

    // **Warehouse Creation Feasibility**
    long existingCountOfWarehousesForLocation = warehouseStore.getNumberOfWarehousesForLocation(warehouse.getLocation());
    if (existingCountOfWarehousesForLocation >= location.getMaxNumberOfWarehouses()) {
      throw new RuntimeException("Number For Warehouses For A Given Location "
              + warehouse.getLocation() + " is already reached");
    }

    // **Capacity and **
    if (warehouse.getCapacity() > location.getMaxCapacity()) {
       throw new RuntimeException("Warehouse capacity " + warehouse.getCapacity() + " cannot exceed the "
               + " maxCapacity" + location.getMaxCapacity() + " of a location");
    }

    // ** Stock Validation **
    if (warehouse.getStock() > warehouse.getCapacity()) {
      throw new RuntimeException("Warehouse stock " + warehouse.getStock() + " cannot be more than"
              + " warehouse capacity");
    }
    warehouseStore.create(warehouse);
  }
}
