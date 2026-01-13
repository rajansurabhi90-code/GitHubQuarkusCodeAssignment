package com.fulfilment.application.monolith.warehouses.domain.models;

import java.time.ZonedDateTime;

public class Warehouse {

  // unique identifier
  private String businessUnitCode;

  private String location;

  private Integer capacity;

  private Integer stock;

  private ZonedDateTime creationAt;

  private ZonedDateTime archivedAt;

  public Warehouse(){}

  public Warehouse(String businessUnitCode, String location, Integer capacity,
                   Integer stock, ZonedDateTime creationAt,  ZonedDateTime archivedAt) {
    this.businessUnitCode = businessUnitCode;
    this.location = location;
    this.capacity = capacity;
    this.stock = stock;
    this.creationAt = creationAt;
    this.archivedAt = archivedAt;
  }

  public void setBusinessUnitCode(String businessUnitCode) {
    this.businessUnitCode = businessUnitCode;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public void setCapacity(Integer capacity) {
    this.capacity = capacity;
  }

  public void setStock(Integer stock) {
    this.stock = stock;
  }

  public void setCreationAt(ZonedDateTime creationAt) {
    this.creationAt = creationAt;
  }

  public void setArchivedAt(ZonedDateTime archivedAt) {
    this.archivedAt = archivedAt;
  }

  public String getLocation() {
    return location;
  }

  public Integer getCapacity() {
    return capacity;
  }

  public String getBusinessUnitCode() {
    return businessUnitCode;
  }

  public Integer getStock() {
    return stock;
  }

  public ZonedDateTime getCreationAt() {
    return creationAt;
  }

  public ZonedDateTime getArchivedAt() {
    return archivedAt;
  }
}
