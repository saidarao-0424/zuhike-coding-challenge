package com.zuhike.coding.challenge.common;

import java.time.format.DateTimeFormatter;

public class Constants {


    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yy");

    public static final String QUERY_CHECK_UNIQUE_ORDER_ID = "select count(ORDER_ID) from STORE_ORDER where ORDER_ID = ?";
    public static final String QUERY_CHECK_UNIQUE_CUSTOMER_ID = "select count(CUSTOMER_ID) from STORE_ORDER where CUSTOMER_ID = ?";
    public static final String QUERY_CHECK_UNIQUE_PRODUCT_ID = "select count(PRODUCT_ID) from STORE_ORDER where PRODUCT_ID = ?";
    public static final String[] CSV_FIELDS = new String[]{"rowID", "orderID", "orderDate", "shipDate", "shipmentMode", "customerID", "customerName", "segment", "country", "city", "state",
            "postalCode", "region", "productID", "category", "subCategory", "productName", "salesAmount", "quantity", "discount", "profit"};

    public static final String INSERT_STORE_ORDERS = "INSERT INTO STORE_ORDER (ORDER_ID, ORDER_DATE, SHIP_DATE, SHIP_MODE, QUANTITY, DISCOUNT, PROFIT, PRODUCT_ID, CUSTOMER_NAME, CATEGORY, CUSTOMER_ID, PRODUCT_NAME ) " +
            "VALUES (:orderID, :orderDate,:shipDate,:shipmentMode,:quantity,:discount,:profit,:productID, :customerName,:category,:customerID,:productName)";


}
