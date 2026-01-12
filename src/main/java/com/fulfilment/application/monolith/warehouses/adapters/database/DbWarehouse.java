package com.fulfilment.application.monolith.warehouses.adapters.database;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "warehouse")
@Cacheable
public class DbWarehouse {

  @Id @GeneratedValue public Long id;

  private String businessUnitCode;

  private String location;

  private Integer capacity;

  private Integer stock;

  private LocalDateTime createdAt;

  private LocalDateTime archivedAt;

  public DbWarehouse() {}

  public void setId(Long id) {
    this.id = id;
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

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public void setArchivedAt(LocalDateTime archivedAt) {
    this.archivedAt = archivedAt;
  }

  public Long getId() {
    return id;
  }

  public String getBusinessUnitCode() {
    return businessUnitCode;
  }

  public String getLocation() {
    return location;
  }

  public Integer getCapacity() {
    return capacity;
  }

  public Integer getStock() {
    return stock;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getArchivedAt() {
    return archivedAt;
  }
}
