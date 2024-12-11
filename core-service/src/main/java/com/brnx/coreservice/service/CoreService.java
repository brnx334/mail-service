package com.brnx.coreservice.service;

import com.brnx.coreservice.model.Message;
import com.brnx.coreservice.model.MessageRabbitAdapter;
import com.brnx.coreservice.model.Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoreService {

    private final MessageService messageService;
    private final TemplateService templateService;
    private final UserService userService;
    private final RabbitTemplate rabbitTemplate;
    private final TemplateProcessor templateProcessor;

    @Value("${rabbitmq.coreQueue}")
    private String coreQueue;

    @RabbitListener(queues = "${rabbitmq.answeringQueue}")
    public void processMessage(Message message) {
        try {
            if (!isTemplateExists(message.getTemplateId())) {
                throw new IllegalArgumentException("Template not found");
            }

            if (!userService.isGroupExists(message.getGroupUsers())) {
                throw new IllegalArgumentException("User's group doesn't exist");
            }

            Template template = templateService.getTemplateById(message.getTemplateId());
            log.info(template.getTemplateText());

            String populatedText = templateProcessor.processTemplate(template.getTemplateText(), message.getData());

            if ("0".equals(populatedText)) {
                throw new IllegalArgumentException("Invalid template data or missing tags");
            }

            message.setMessageText(populatedText);
            message.setStatus("PROCESSED");

            List<String> usersEmails = userService.getAllUsersEmailByGroupId(message.getGroupUsers());
            if (usersEmails.isEmpty()) {
                throw new IllegalArgumentException("No users found in the specified group");
            }

            MessageRabbitAdapter messageRabbitAdapter = new MessageRabbitAdapter();
            messageRabbitAdapter.setUniqueMessage(message.getUniqueMessage());
            messageRabbitAdapter.setMessageText(message.getMessageText());
            messageRabbitAdapter.setMailList(usersEmails);
            messageRabbitAdapter.setStatus(message.getStatus());

            messageService.updateMessage(message);
            log.info(message.getUniqueMessage() + " - saved by core-service with status: " + message.getStatus());

            rabbitTemplate.convertAndSend(coreQueue, messageRabbitAdapter);
            log.info(message.getUniqueMessage() + " delivered to smtp-mail-service from core-service with status: "
                    + messageRabbitAdapter.getStatus());
        } catch (IllegalArgumentException e) {
            handleError(message, e.getMessage());
        } catch (Exception e) {
            handleError(message, "An unexpected error occurred: " + e.getMessage());
            log.error("Unexpected error: ", e);
        }
    }

    private void handleError(Message message, String errorMessage) {
        message.setErrors(errorMessage);
        message.setStatus("ERROR");
        messageService.updateMessage(message);
        log.error("Error processing message {}: {}", message.getUniqueMessage(), errorMessage);
    }

    private boolean isTemplateExists(Long templateID) {
        return templateService.isTemplateExists(templateID);
    }

    @RabbitListener(queues = "${rabbitmq.smtpStatusQueue}")
    public void updateMessageStatus(MessageRabbitAdapter messageStatusUpdate) {
        // Получаем сообщение по уникальному идентификатору через MessageService
        Message message = messageService.getMessagesByUniqueMessage(messageStatusUpdate.getUniqueMessage());
        if (message == null) {
            log.error("Message with unique ID " + messageStatusUpdate.getUniqueMessage() + " not found");
            return;
        }

        // Обновляем статус сообщения
        message.setStatus(messageStatusUpdate.getStatus());
        message.setErrors(messageStatusUpdate.getErrors());
        message.setDateStatus(LocalDateTime.now());

        // Сохраняем обновлённое сообщение через MessageService
        messageService.updateMessage(message);
        log.info("Message " + messageStatusUpdate.getUniqueMessage() + " from smtp-mail-service has been updated in DB with status: "
                + messageStatusUpdate.getStatus());
    }
}

