package com.brnx.coreservice.service;

import com.brnx.coreservice.mapper.MessageMapper;
import com.brnx.coreservice.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageMapper messageMapper;

    public void createMessage(Message message) {
        try {
            messageMapper.insertMessage(message);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error while creating a message: " + e.getMessage(), e);
        }
    }

    public Message getMessageById(Long id) {
        Message message = messageMapper.selectMessageById(id);
        if (message == null) {
            throw new IllegalArgumentException("Message with ID " + id + " not found");
        }
        return message;
    }

    public void updateMessageStatus(Long id, String status) {
        try {
            if (messageMapper.selectMessageById(id) == null) {
                throw new IllegalArgumentException("Message with ID " + id + " not found");
            }
            messageMapper.updateMessageStatus(id, status, LocalDateTime.now());
        } catch (DataAccessException e) {
            throw new RuntimeException("Error when updating message status with ID " + id + ": " + e.getMessage(), e);
        }
    }

    public void updateMessage(Message message) {
        try {
            messageMapper.updateMessage(
                    message.getUniqueMessage(),
                    message.getStatus(),
                    message.getErrors(),
                    message.getMessageText(),
                    message.getDateStatus() != null ? message.getDateStatus() : LocalDateTime.now()
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to update message with uniqueMessage: " + message.getUniqueMessage(), e);
        }
    }

    public void deleteMessage(Long id) {
        try {
            if (messageMapper.selectMessageById(id) == null) {
                throw new IllegalArgumentException("Message with ID " + id + " not found");
            }
            messageMapper.deleteMessage(id);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error when updating message status with ID " + id + ": " + e.getMessage(), e);
        }
    }

    public Message getMessagesByUniqueMessage(String uniqueMessage) {
        try {
            Message message = messageMapper.findByUniqueMessage(uniqueMessage);
            if (message == null) {
                throw new IllegalArgumentException("Message with uniqueMessage " + uniqueMessage + " don't found");
            }
            return message;
        } catch (DataAccessException e) {
            throw new RuntimeException("Error when searching for a message with a unique message " + uniqueMessage + ": " + e.getMessage(), e);
        }
    }
}
