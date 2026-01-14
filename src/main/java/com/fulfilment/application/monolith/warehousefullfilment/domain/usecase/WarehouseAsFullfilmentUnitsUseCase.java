package com.fulfilment.application.monolith.warehousefullfilment.domain.usecase;

import com.fulfilment.application.monolith.warehousefullfilment.Exception.WarehouseFullfilmentFailedException;
import com.fulfilment.application.monolith.warehousefullfilment.domain.model.WarehouseFullfilment;
import com.fulfilment.application.monolith.warehousefullfilment.domain.port.WarehouseAsFullfilmentUnitsOperation;
import com.fulfilment.application.monolith.warehousefullfilment.domain.port.WarehouseFullfilmentStore;
import com.fulfilment.application.monolith.warehouses.Constants;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class WarehouseAsFullfilmentUnitsUseCase implements
        WarehouseAsFullfilmentUnitsOperation {
    private static final Logger logger = LoggerFactory.getLogger(WarehouseAsFullfilmentUnitsUseCase.class);
    private final WarehouseFullfilmentStore warehouseFullfilmentStore;

    public WarehouseAsFullfilmentUnitsUseCase(WarehouseFullfilmentStore warehouseFullfilmentStore) {
        this.warehouseFullfilmentStore = warehouseFullfilmentStore;
    }

    @Override
    public void createWarehouseFullfilment(WarehouseFullfilment warehouseFullfilment) throws WarehouseFullfilmentFailedException {
        try {
            // max 2 warehouses per product per store
            long warehousesPerProduct = warehouseFullfilmentStore.findNumberofWarehousesForAProductPerStore(warehouseFullfilment);
            if (warehousesPerProduct > 2) {
                logger.error(Constants.PRODUCTS_PER_WAREHOUSE_EXCEEDED);
                throw new WarehouseFullfilmentFailedException(Constants.PRODUCTS_PER_WAREHOUSE_EXCEEDED);
            }
            //  max 3 warehouses per store
            long warehousePerStore = warehouseFullfilmentStore.findNumberofWarehousesPerStore(warehouseFullfilment);
            if (warehousePerStore > 3) {
                logger.error(Constants.WAREHOUSE_PER_STORE_EXCEEDED);
                throw new WarehouseFullfilmentFailedException(Constants.WAREHOUSE_PER_STORE_EXCEEDED);
            }
            //max 5 products per warehouse
            long warehousePerProduct = warehouseFullfilmentStore.findNumberofWarehousesPerproduct(warehouseFullfilment);
            if (warehousePerProduct > 5) {
                logger.error(Constants.WAREHOUSE_PER_PRODUCT_EXCEEDED);
                throw new WarehouseFullfilmentFailedException(Constants.WAREHOUSE_PER_PRODUCT_EXCEEDED);
            }
            warehouseFullfilmentStore.create(warehouseFullfilment);
            logger.info("warehouse fullfilment succeeded");
        } catch (Exception e) {
            logger.error("Unable to accomplish warehouse fullfilment" + e.getMessage());
            throw e;
        }
    }
}
