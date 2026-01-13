package com.fulfilment.application.monolith.warehousefullfilment.domain.usecase;

import com.fulfilment.application.monolith.warehousefullfilment.domain.model.WarehouseFullfilment;
import com.fulfilment.application.monolith.warehousefullfilment.domain.port.WarehouseAsFullfilmentUnitsOperation;
import com.fulfilment.application.monolith.warehousefullfilment.domain.port.WarehouseFullfilmentStore;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WarehouseAsFullfilmentUnitsUseCase implements
        WarehouseAsFullfilmentUnitsOperation {
    private final WarehouseFullfilmentStore warehouseFullfilmentStore;

    public WarehouseAsFullfilmentUnitsUseCase(WarehouseFullfilmentStore warehouseFullfilmentStore) {
        this.warehouseFullfilmentStore = warehouseFullfilmentStore;
    }

    @Override
    public void createWarehouseFullfilment(WarehouseFullfilment warehouseFullfilment) {
      // max 2 warehouses per product per store
      long warehousesPerProduct = warehouseFullfilmentStore.findNumberofWarehousesForAProductPerStore(warehouseFullfilment);
        if (warehousesPerProduct > 2) {
            throw new RuntimeException("Product already assigned to 2 warehouses for this store");
        }
      //  max 3 warehouses per store
      long warehousePerStore = warehouseFullfilmentStore.findNumberofWarehousesPerStore(warehouseFullfilment);
      if (warehousePerStore > 3) {
            throw new RuntimeException("Store already has 3 warehouses assigned");
      }
      //max 5 products per warehouse
      long warehousePerProduct = warehouseFullfilmentStore.findNumberofWarehousesPerproduct(warehouseFullfilment);
      if (warehousePerProduct > 5) {
          throw new RuntimeException("Warehouse already stores 5 product types");
      }
        warehouseFullfilmentStore.create(warehouseFullfilment);
    }
}
