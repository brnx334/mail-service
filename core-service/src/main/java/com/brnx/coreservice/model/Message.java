package com.brnx.coreservice.model;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class Message {
    private Long id;
    private String uniqueMessage;
    private Integer groupUsers;
    private Long templateId;
    private byte[] file;
    private String typeFile;
    private String errors;
    private String messageText;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime dateStatus;
    private JsonNode data;
}