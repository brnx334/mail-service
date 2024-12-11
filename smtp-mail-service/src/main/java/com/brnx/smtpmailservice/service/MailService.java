package com.brnx.smtpmailservice.service;

import com.brnx.smtpmailservice.model.MessageRabbitAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MailService {

    @Value("${spring.rabbitmq.smtpMailQueue}")
    private String smtpStatusQueue;

    @Autowired
    public RabbitTemplate rabbitTemplate;


    @RabbitListener(queues = {"${spring.rabbitmq.coreServiceQueue}"})
    public void receiveMessage(MessageRabbitAdapter message) {

        log.info("message: " + message.getUniqueMessage() + " - delivered");

        message.setStatus("DELIVERED");
        log.info(message.toString());
        rabbitTemplate.convertAndSend(smtpStatusQueue, message);
    }
}
