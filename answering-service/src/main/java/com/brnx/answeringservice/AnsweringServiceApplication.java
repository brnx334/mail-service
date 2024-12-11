package com.brnx.answeringservice;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AnsweringServiceApplication {

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private Queue queue;

    @PostConstruct
    public void declareQueue(){
        rabbitAdmin.declareQueue(queue);
    }


    public static void main(String[] args) {
        SpringApplication.run(AnsweringServiceApplication.class, args);
    }

}
