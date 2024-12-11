package com.brnx.answeringservice.model;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;


@Data
public class MessageDto {
    private String uniqueMessage;
    private Integer groupUsers;
    private Long templateId;
    private String file; // file Base64
    private String typeFile;
    private JsonNode data;
}