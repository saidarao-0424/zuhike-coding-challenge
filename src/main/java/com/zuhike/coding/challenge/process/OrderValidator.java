package com.zuhike.coding.challenge.process;

import com.zuhike.coding.challenge.model.StoreOrderDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import static com.zuhike.coding.challenge.common.Constants.*;

@Component
@Slf4j
public class OrderValidator {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public OrderValidator(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean validate(final StoreOrderDTO storeOrder) {
        int orderIdCount = jdbcTemplate.queryForObject(QUERY_CHECK_UNIQUE_ORDER_ID, new Object[]{storeOrder.getOrderID()}, Integer.class);
        if (orderIdCount > 0) {
            log.warn("Store Order Record will be ignored due to duplicate Order ID | {}", storeOrder.getOrderID());
            return false;
        }
        int productIdCount = jdbcTemplate.queryForObject(QUERY_CHECK_UNIQUE_PRODUCT_ID, new Object[]{storeOrder.getProductID()}, Integer.class);
        if (productIdCount > 0) {
            log.warn("Store Order Record will be ignored due to duplicate Product ID | {}", storeOrder.getProductID());
            return false;
        }
        int custIdCount = jdbcTemplate.queryForObject(QUERY_CHECK_UNIQUE_CUSTOMER_ID, new Object[]{storeOrder.getCustomerID()}, Integer.class);
        if (custIdCount > 0) {
            log.warn("Store Order Record will be ignored due to duplicate Customer ID |{}", storeOrder.getCustomerID());
            return false;
        }
        return true;
    }
}
