package com.fulfilment.application.monolith.warehouses.adapters.restapi;


import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.usecases.*;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.verify;

@QuarkusTest
public class WarehouseResourceImplTest {
  @InjectMock
  CreateWarehouseUseCase createWarehouseUseCase;

  @InjectMock
  ReplaceWarehouseUseCase replaceWarehouseUseCase;

  @InjectMock
  ArchiveWarehouseUseCase archiveWarehouseUseCase;

  @InjectMock
  GetWarehouseUnitByIdUseCase getWarehouseUnitByIdUseCase;

  @InjectMock
  ArchiveWarehouseUnitByIdUseCase archiveWarehouseUnitByIdUseCase;

  @InjectMock
  ListAllWarehouseUseCase listAllWarehouseUseCase;

  @InjectMock
  RemoveWarehouseUseCase removeWarehouseUseCase;

  private WarehouseResourceImpl warehouseResourceImpl;

  @BeforeEach
  void setUp() {
    warehouseResourceImpl = new WarehouseResourceImpl(
            createWarehouseUseCase,
            replaceWarehouseUseCase,
            archiveWarehouseUseCase,
            getWarehouseUnitByIdUseCase,
            archiveWarehouseUnitByIdUseCase,
            listAllWarehouseUseCase,
            removeWarehouseUseCase
    );
  }


  @Test
  public void testWhenCreateANewWarehouseUnitSuccess() {
    Warehouse warehouse = new Warehouse("MHW.000",
          "ZWOLLE-001", 10, 10, null, null);
    warehouseResourceImpl.createANewWarehouseUnit(warehouse);
    verify(createWarehouseUseCase).create(warehouse);
  }

  @Test
  public void testWhenReplaceAWarehouseUnitSuccess() {
    Warehouse warehouse = new Warehouse("MHW.000",
            "ZWOLLE-001", 50, 10, null, null);
    warehouseResourceImpl.replaceAWareHouseUnit(warehouse);
    verify(replaceWarehouseUseCase).replace(warehouse);
  }

  @Test
  public void testWhenArchiveAWarehouseUnitSuccess() {
    Warehouse warehouse = new Warehouse("MHW.000",
            "ZWOLLE-001", 50, 10, null, null);
    warehouseResourceImpl.archiveAWareHouseUnit(warehouse);
    verify(archiveWarehouseUseCase).archive(warehouse);
  }

  @Test
  public void testWhenRemoveAWarehouseUnitSuccess() {
    warehouseResourceImpl.removeAWareHouseUnit(1L);
    verify(removeWarehouseUseCase).removeById(1L);
  }

  @Test
  public void testWhenGetWarehouseUnitByIdSuccess() {
    warehouseResourceImpl.getAWarehouseUnitByID("3");
    verify(getWarehouseUnitByIdUseCase).getWarehouseUnitById("3");
  }

  @Test
  public void testWhenArchiveWarehouseUnitByIdSuccess() {
    warehouseResourceImpl.archiveAWarehouseUnitByID("3");
    verify(archiveWarehouseUnitByIdUseCase).archiveWareHouseUnitById("3");
  }

  @Test
  public void testWhenListAllWarehousesSuccess() {
    warehouseResourceImpl.listAllWarehousesUnits();
    verify(listAllWarehouseUseCase).getAllWarehouses();
  }
}
