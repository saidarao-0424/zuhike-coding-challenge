package com.zuhike.coding.challenge.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class StoreOrderDTO implements Serializable {

    private String rowID;
    private String orderID;
    private String orderDate;
    private String shipDate;
    private String shipmentMode;
    private String customerID;
    private String customerName;
    private String segment;
    private String country;
    private String city;
    private String state;
    private String postalCode;
    private String region;
    private String productID;
    private String category;
    private String subCategory;
    private String productName;
    private String salesAmount;
    private String quantity;
    private String discount;
    private String profit;

}
