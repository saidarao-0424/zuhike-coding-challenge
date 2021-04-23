package com.zuhike.coding.challenge.model;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 */
@Data
@ToString
@Builder
public class StoreOrder implements Serializable {

    private String orderID;
    private LocalDate orderDate;
    private LocalDate shipDate;
    private String shipmentMode;
    private int quantity;
    private BigDecimal discount;
    private BigDecimal profit;
    private String productID;
    private String customerName;
    private String category;
    private String customerID;
    private String productName;

}
