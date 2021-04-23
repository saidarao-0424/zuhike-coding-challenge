package com.zuhike.coding.challenge.process;

import com.zuhike.coding.challenge.model.StoreOrder;
import com.zuhike.coding.challenge.model.StoreOrderDTO;
import org.springframework.batch.item.ItemProcessor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StoreOrderProcessor implements ItemProcessor<StoreOrderDTO, StoreOrder> {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.mm.yy");

    @Override
    public StoreOrder process(StoreOrderDTO storeOrder) throws Exception {
        //Validate the record for Validity
        return StoreOrder.builder().orderID(storeOrder.getOrderID())
                .orderDate(LocalDate.parse(storeOrder.getOrderDate(), formatter))
                .shipDate(LocalDate.parse(storeOrder.getShipDate(), formatter))
                .shipmentMode(storeOrder.getShipmentMode())
                .customerID(storeOrder.getCustomerID()).customerName(storeOrder.getCustomerName())
                .productID(storeOrder.getProductID()).category(storeOrder.getCategory())
                .productName(storeOrder.getProductName())
                .quantity(Integer.valueOf(storeOrder.getQuantity()))
                .discount(new BigDecimal(storeOrder.getDiscount())).profit(new BigDecimal(storeOrder.getProfit()))
                .build();
    }
}
