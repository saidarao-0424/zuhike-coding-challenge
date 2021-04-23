package com.zuhike.coding.challenge.process;

import com.zuhike.coding.challenge.model.StoreOrder;
import com.zuhike.coding.challenge.model.StoreOrderDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.zuhike.coding.challenge.common.Constants.dateTimeFormatter;

@Slf4j
@Component
public class StoreOrderProcessor implements ItemProcessor<StoreOrderDTO, StoreOrder> {

    @Autowired
    OrderValidator orderValidator;

    @Override
    public StoreOrder process(final StoreOrderDTO storeOrder) throws Exception {
        //Validate the record for Validity
        log.info("========== Started Processing StoreOrder {} =========== ", storeOrder);

        if (!orderValidator.validate(storeOrder)) {
            return null;
        }

        return StoreOrder.builder().orderID(storeOrder.getOrderID())
                .orderDate(LocalDate.parse(storeOrder.getOrderDate(), dateTimeFormatter))
                .shipDate(LocalDate.parse(storeOrder.getShipDate(), dateTimeFormatter))
                .shipmentMode(storeOrder.getShipmentMode())
                .customerID(storeOrder.getCustomerID()).customerName(storeOrder.getCustomerName())
                .productID(storeOrder.getProductID()).category(storeOrder.getCategory())
                .productName(storeOrder.getProductName())
                .quantity(Integer.valueOf(storeOrder.getQuantity()))
                .discount(new BigDecimal(storeOrder.getDiscount())).profit(new BigDecimal(storeOrder.getProfit()))
                .build();
    }
}
