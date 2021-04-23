package com.zuhike.coding.challenge.config;

import com.zuhike.coding.challenge.model.StoreOrder;
import com.zuhike.coding.challenge.model.StoreOrderDTO;
import com.zuhike.coding.challenge.process.StoreOrderListener;
import com.zuhike.coding.challenge.process.StoreOrderProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class StoreOrderBatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;

    @Bean
    public FlatFileItemReader<StoreOrderDTO> storeOrderReader() {
        FlatFileItemReader<StoreOrderDTO> reader = new FlatFileItemReader<StoreOrderDTO>();
        reader.setResource(new ClassPathResource("sales.csv"));

        reader.setLineMapper(new DefaultLineMapper<StoreOrderDTO>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[]{"Row ID", "Order ID", "Order Date", "Ship Date", "Ship Mode", "Customer ID", "Customer Name", "Segment", "Country", "City", "State",
                        "Postal Code", "Region", "Product ID", "Category", "Sub-Category", "Product Name", "Sales", "Quantity", "Discount", "Profit"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper() {{
                setTargetType(StoreOrderDTO.class);
            }});
        }});
        return reader;
    }


    @Bean
    public StoreOrderProcessor storeOrderProcessor() {
        return new StoreOrderProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<StoreOrder> storeOrderWriter() {
        JdbcBatchItemWriter<StoreOrder> writer = new JdbcBatchItemWriter<StoreOrder>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("INSERT INTO STORE_ORDER (ORDER_ID, ORDER_DATE, SHIP_DATE, SHIP_MODE, QUANTITY, DISCOUNT, PROFIT, PRODUCT_ID, CUSTOMER_NAME, CATEGORY, CUSTOMER_ID, PRODUCT_NAME ) " +
                "VALUES (:orderID, :orderDate,:shipDate,:shipmentMode,:quantity,:discount,:profit,:productID, :customerName,:category,:customerID,:productName)");
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public Job processStoreOrdersJob(StoreOrderListener listener) {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(processStoreOrdersStep())
                .end()
                .build();
    }

    @Bean
    public Step processStoreOrdersStep() {
        return stepBuilderFactory.get("step1")
                .<StoreOrderDTO, StoreOrder>chunk(10)
                .reader(storeOrderReader())
                .processor(storeOrderProcessor())
                .writer(storeOrderWriter())
                .build();
    }

}
