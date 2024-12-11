package com.brnx.coreservice;

import jakarta.annotation.PostConstruct;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.brnx.coreservice.mapper")
public class CoreServiceApplication {

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @PostConstruct
    public void declareQueues() {
        rabbitAdmin.initialize();
    }

    public static void main(String[] args) {
        SpringApplication.run(CoreServiceApplication.class, args);
    }
}
