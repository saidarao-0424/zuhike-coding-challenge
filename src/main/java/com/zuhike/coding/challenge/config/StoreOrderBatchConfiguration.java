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
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

import static com.zuhike.coding.challenge.common.Constants.CSV_FIELDS;
import static com.zuhike.coding.challenge.common.Constants.INSERT_STORE_ORDERS;

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

        FlatFileItemReader<StoreOrderDTO> flatFileItemReader = new FlatFileItemReaderBuilder<StoreOrderDTO>()
                .name("personItemReader")
                .resource(new ClassPathResource("sales.csv"))
                .delimited()
                .names(CSV_FIELDS)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<StoreOrderDTO>() {{
                    setTargetType(StoreOrderDTO.class);
                }})
                .build();
        flatFileItemReader.setLinesToSkip(1);
        return flatFileItemReader;
    }


    @Bean
    public JdbcBatchItemWriter<StoreOrder> storeOrderWriter() {
        JdbcBatchItemWriter<StoreOrder> writer = new JdbcBatchItemWriter<StoreOrder>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql(INSERT_STORE_ORDERS);
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public Job processStoreOrdersJob(StoreOrderListener listener, @Autowired StoreOrderProcessor storeOrderProcessor) {
        return jobBuilderFactory.get("processStoreOrdersJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(processStoreOrdersStep(storeOrderProcessor))
                .end()
                .build();
    }

    @Bean
    public Step processStoreOrdersStep(StoreOrderProcessor storeOrderProcessor) {
        return stepBuilderFactory.get("processStoreOrdersStep")
                .<StoreOrderDTO, StoreOrder>chunk(1)
                .reader(storeOrderReader())
                .processor(storeOrderProcessor)
                .writer(storeOrderWriter())
                .build();
    }

}
