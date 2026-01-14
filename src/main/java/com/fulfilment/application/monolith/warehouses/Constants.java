package com.fulfilment.application.monolith.warehouses;

public class Constants {
    public static final String WAREHOUSE_ALREADY_EXISTS = "Warehouses with given business code %s already Exists";
    public static final String WAREHOUSE_DOES_NOT_EXISTS = "Warehouse with given business code %s does not Exists to archive";
    public static final String NOT_VALID_LOCATION = "Invalid Location %s Doesnt Exist";
    public static final String WAREHOUSE_EXCEEDED_PER_LOCATION = "Number For Warehouses For A Given Location %s is already reached";
    public static final String WAREHOUSE_CAPACITY_EXCEEDED_LOCATION_CAPACITY = "Warehouse capacity %s cannot exceed the maxCapacity %s of a location";
    public static final String WAREHOUSE_STOCK_EXCEEDED_WAREHOUSE_CAPACITY = "Warehouse stock %s cannot be more than warehouse capacity";
    public static final String ACTIVE_WAREHOUSE_NOT_FOUND = "Active Warehouse with given business code %s does not Exists to replace";
    public static final String NEWWAREHOUSE_CAPACITY_NOT_ENOUGH = "Capacity of the stock being replaced cannot accomodate stock from previous warehouse";
    public static final String STOCKS_ARENT_SAME = "Stock from warehouse being replaced is not same";
    public static final String PRODUCTS_PER_WAREHOUSE_EXCEEDED = "Product already assigned to 2 warehouses for this store";
    public static final String WAREHOUSE_PER_STORE_EXCEEDED = "Store already has 3 warehouses assigned";
    public static final String WAREHOUSE_PER_PRODUCT_EXCEEDED = "Warehouse already stores 5 product types";
}
