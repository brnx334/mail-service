package com.brnx.answeringservice.controller;

import com.brnx.answeringservice.model.Message;
import com.brnx.answeringservice.model.MessageDto;
import com.brnx.answeringservice.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@Slf4j
@RestController
@RequestMapping("")
@RequiredArgsConstructor
@Tag(name = "Answering Service", description = "API для управления сообщениями и очередями")
public class AnsweringController {

    private final MessageService messageService;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.queueName}")
    private String answeringServiceQueue;

    @Operation(summary = "Создать сообщение", description = "Создаёт новое сообщение, сохраняет его в базе данных и отправляет в очередь.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Сообщение успешно создано"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные в запросе"),
            @ApiResponse(responseCode = "503", description = "Сервис временно недоступен")
    })
    @PostMapping(value = "/sent", consumes = "application/json")
    public ResponseEntity<String> createMessageRequest(@RequestBody MessageDto messageDto) {
        if (!isValidMessage(messageDto)) {
            return ResponseEntity.badRequest().body("Invalid data in request");
        }

        Message message = convertDtoToEntity(messageDto);

        try {
            messageService.createMessage(message);
        } catch (Exception e) {
            log.error("Failed to save message to the database", e);
            log.error("Message data: {}", message);
            return ResponseEntity.status(503).body("Service unavailable, please try again later");
        }

        try {
            rabbitTemplate.convertAndSend(answeringServiceQueue, message);
            log.info("Message {} received by core-service", message.getUniqueMessage());
        } catch (Exception e) {
            log.error("Failed to send message to queue", e);
            return ResponseEntity.status(503).body("Failed to send message to queue");
        }

        return ResponseEntity.ok("Message request created successfully");
    }

    @Operation(summary = "Получить сообщение", description = "Возвращает сообщение по уникальному идентификатору.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Сообщение найдено",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Message.class))),
            @ApiResponse(responseCode = "204", description = "Сообщение не найдено"),
            @ApiResponse(responseCode = "206", description = "Частичный ответ при возникновении ошибки")
    })
    @GetMapping("/answer/{uniqueMessage}")
    public ResponseEntity<Message> getMessageByUniqueMessage(@PathVariable String uniqueMessage) {
        var message = messageService.getMessagesByUniqueMessage(uniqueMessage);

        if (message == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

        try {
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            message.setErrors("Ошибка при обработке сообщения: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(message);
        }
    }

    private boolean isValidMessage(MessageDto messageDto) {
        return messageDto.getUniqueMessage() != null &&
                messageDto.getGroupUsers() != null &&
                messageDto.getTemplateId() != null;
    }

    private Message convertDtoToEntity(MessageDto messageDto) {
        Message message = new Message();
        message.setUniqueMessage(messageDto.getUniqueMessage());
        message.setGroupUsers(messageDto.getGroupUsers());
        message.setTemplateId(messageDto.getTemplateId());
        message.setStatus("RECEIVED");
        message.setTypeFile(messageDto.getTypeFile());
        message.setData(messageDto.getData());

        if (messageDto.getFile() != null) {
            try {
                byte[] fileBytes = Base64.getDecoder().decode(messageDto.getFile());
                message.setFile(fileBytes);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid Base64 encoding for file", e);
            }
        }

        return message;
    }
}
