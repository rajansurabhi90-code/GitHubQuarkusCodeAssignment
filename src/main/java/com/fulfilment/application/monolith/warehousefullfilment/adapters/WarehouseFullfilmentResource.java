package com.fulfilment.application.monolith.warehousefullfilment.adapters;

import com.fulfilment.application.monolith.warehousefullfilment.Exception.WarehouseFullfilmentFailedException;
import com.fulfilment.application.monolith.warehousefullfilment.domain.model.WarehouseFullfilment;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import jakarta.validation.constraints.NotNull;

public interface WarehouseFullfilmentResource {
    WarehouseFullfilment assignWarehouseToProduct(@NotNull WarehouseFullfilment data) throws WarehouseFullfilmentFailedException;
}
