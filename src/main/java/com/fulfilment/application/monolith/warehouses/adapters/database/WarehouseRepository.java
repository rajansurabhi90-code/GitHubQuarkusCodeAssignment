package com.fulfilment.application.monolith.warehouses.adapters.database;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import edu.umd.cs.findbugs.annotations.NonNull;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class WarehouseRepository implements WarehouseStore, PanacheRepository<DbWarehouse> {

    @Override
    public void create(Warehouse warehouse) {
        warehouse.setCreationAt(getZonedDateTime());
        persist(entityFromWareHouse(warehouse));
    }

    @NonNull
    private static ZonedDateTime getZonedDateTime() {
        return LocalDateTime.now().atZone(ZoneOffset.UTC);
    }

    @Override
    public void update(Warehouse oldWarehouse) {
       oldWarehouse.setArchivedAt(getZonedDateTime());
       persist(entityFromWareHouse(oldWarehouse));
    }

    @Override
    public void remove(Warehouse warehouse) {
      delete(entityFromWareHouse(warehouse));
    }

    @Override
    public List<Warehouse> findByBusinessUnitCode(String buCode) {
        List<DbWarehouse> dbWarehouseList = find("businessUnitCode", buCode).list();
        if (dbWarehouseList.isEmpty()) {
            return Collections.emptyList();
        }
        return getWarehouseList(dbWarehouseList);
    }

    @NonNull
    private List<Warehouse> getWarehouseList(List<DbWarehouse> dbWarehouseList) {
        return dbWarehouseList.stream()
                .map(this::warehouseFromEntity)
                .toList();
    }

    @Override
    public  Warehouse getActiveWarehouse(List<Warehouse> warehouses) {
        return warehouses.stream()
                .filter(warehouse -> warehouse.getArchivedAt() == null)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No Active Warehouses Found"));
    }

    @Override
    public long getNumberOfWarehousesForLocation(String location) {
        List<DbWarehouse> activeDbWareHouses = find("location", location)
                 .list().stream()
                .filter(dbWarehouse -> dbWarehouse.getArchivedAt()== null)
                .toList();
        return activeDbWareHouses.size();
    }

    @Override
    public Warehouse findById(String id) {
        DbWarehouse dbWarehouse =  find("id", id).firstResult();
        if (dbWarehouse != null) {
            return warehouseFromEntity(dbWarehouse);
        }
        return null;
    }

    @Override
    public List<Warehouse> findAllWarehouses() {
        return getWarehouseList(listAll());
    }

    private DbWarehouse entityFromWareHouse(Warehouse warehouse) {
        DbWarehouse entity = new DbWarehouse();
        entity.setBusinessUnitCode(warehouse.getBusinessUnitCode());
        entity.setLocation(warehouse.getLocation());
        entity.setCapacity(warehouse.getCapacity());
        entity.setStock(warehouse.getStock());
        entity.setCreatedAt(getLocalDate(warehouse.getCreationAt()));
        entity.setArchivedAt(getLocalDate(warehouse.getArchivedAt()));
        return entity;
    }

    private Warehouse warehouseFromEntity(DbWarehouse dbWarehouse) {
        return new Warehouse(
        dbWarehouse.getBusinessUnitCode(), dbWarehouse.getLocation(), dbWarehouse.getCapacity(),
        dbWarehouse.getStock(), dbWarehouse.getCreatedAt().atZone(ZoneOffset.UTC),
        dbWarehouse.getArchivedAt() != null
                ? dbWarehouse.getArchivedAt().atZone(ZoneOffset.UTC)
                : null);
    }

    private LocalDateTime getLocalDate(ZonedDateTime createdOrArchivedAt) {
        if (createdOrArchivedAt != null) {
            return createdOrArchivedAt.toLocalDateTime();
        }
        return null;
    }
}

