package com.brnx.smtpmailservice.model;

import lombok.Data;

import java.util.List;


@Data
public class MessageRabbitAdapter {

    private String uniqueMessage;
    private String messageText;
    private List<String> mailList;
    private String status;
    private String errors;

}
