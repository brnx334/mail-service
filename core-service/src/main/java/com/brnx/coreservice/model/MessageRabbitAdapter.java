package com.brnx.coreservice.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class MessageRabbitAdapter {

    private String uniqueMessage;
    private String messageText;
    private List<String> mailList;
    private String status;
    private String errors;

}