package com.fulfilment.application.monolith.warehouses.adapters.restapi;

//import com.warehouse.api.WarehouseResource;
//import com.warehouse.api.beans.Warehouse;

import com.fulfilment.application.monolith.warehouses.adapters.WarehouseResource;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.usecases.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@ApplicationScoped
public class WarehouseResourceImpl implements WarehouseResource {

  private final CreateWarehouseUseCase createWarehouseUseCase;
  private final ReplaceWarehouseUseCase replaceWarehouseUseCase;
  private final ArchiveWarehouseUseCase archiveWarehouseUseCase;
  private final GetWarehouseUnitByIdUseCase getWarehouseUnitByIdUseCase;
  private final ArchiveWarehouseUnitByIdUseCase archiveWarehouseUnitByIdUseCase;
  private final ListAllWarehouseUseCase listAllWarehouseUseCase;
  private final RemoveWarehouseUseCase removeWarehouseUseCase;

  public WarehouseResourceImpl(CreateWarehouseUseCase createWarehouseUseCase,
                               ReplaceWarehouseUseCase replaceWarehouseUseCase,
                               ArchiveWarehouseUseCase archiveWarehouseUseCase,
                               GetWarehouseUnitByIdUseCase getWarehouseUnitByIdUseCase,
                               ArchiveWarehouseUnitByIdUseCase archiveWarehouseUnitByIdUseCase,
                               ListAllWarehouseUseCase listAllWarehouseUseCase,
                               RemoveWarehouseUseCase removeWarehouseUseCase) {
    this.createWarehouseUseCase = createWarehouseUseCase;
    this.replaceWarehouseUseCase = replaceWarehouseUseCase;
    this.archiveWarehouseUseCase = archiveWarehouseUseCase;
    this.getWarehouseUnitByIdUseCase = getWarehouseUnitByIdUseCase;
    this.archiveWarehouseUnitByIdUseCase = archiveWarehouseUnitByIdUseCase;
    this.listAllWarehouseUseCase = listAllWarehouseUseCase;
    this.removeWarehouseUseCase = removeWarehouseUseCase;
  }

  @Override
  public List<Warehouse> listAllWarehousesUnits() {
    return listAllWarehouseUseCase.getAllWarehouses();
  }

  @Override
  public void createANewWarehouseUnit(@NotNull Warehouse data) {
    createWarehouseUseCase.create(data);
  }

  @Override
  public void replaceAWareHouseUnit(@NotNull Warehouse data) {
    replaceWarehouseUseCase.replace(data);
  }

  @Override
  public void archiveAWareHouseUnit(@NotNull Warehouse data) {
    archiveWarehouseUseCase.archive(data);
  }

  @Override
  public Warehouse getAWarehouseUnitByID(String id) {
    return getWarehouseUnitByIdUseCase.getWarehouseUnitById(id);
  }

  @Override
  public void archiveAWarehouseUnitByID(String id) {
    archiveWarehouseUnitByIdUseCase.archiveWareHouseUnitById(id);
  }

  @Override
  public void removeAWareHouseUnit(@NotNull Long id) {
    removeWarehouseUseCase.removeById(id);
  }
}
