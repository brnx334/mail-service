package com.brnx.coreservice.mapper;

import com.brnx.coreservice.model.Message;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Mapper
@Repository
public interface MessageMapper {

    @Insert("""
                INSERT INTO messages (
                    unique_message, group_users, template_id, file, type_file, errors, 
                    message_text, status, created_at, date_status, data
                ) VALUES (
                    #{uniqueMessage}, #{groupUsers}, #{templateId}, #{file}, #{typeFile}, #{errors}, 
                    #{messageText}, #{status}, #{createdAt}, #{dateStatus}, CAST(#{data} AS JSONB)
                )
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertMessage(Message message);

    @Select("""
                SELECT id, unique_message, group_users, template_id, file, type_file, errors, 
                       message_text, status, created_at, date_status, data::TEXT AS data
                FROM messages
                WHERE id = #{id}
            """)
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "uniqueMessage", column = "unique_message"),
            @Result(property = "groupUsers", column = "group_users"),
            @Result(property = "templateId", column = "template_id"),
            @Result(property = "file", column = "file"),
            @Result(property = "typeFile", column = "type_file"),
            @Result(property = "errors", column = "errors"),
            @Result(property = "messageText", column = "message_text"),
            @Result(property = "status", column = "status"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "dateStatus", column = "date_status"),
            @Result(property = "data", column = "data", typeHandler = com.brnx.coreservice.config.JsonTypeHandler.class)
    })
    Message selectMessageById(Long id);

    @Update("""
                UPDATE messages
                SET status = #{status}, date_status = #{dateStatus}
                WHERE id = #{id}
            """)
    void updateMessageStatus(
            @Param("id") Long id,
            @Param("status") String status,
            @Param("dateStatus") LocalDateTime dateStatus);

    @Update("""
        UPDATE messages
        SET 
            status = #{status},
            errors = #{errors},
            message_text = #{messageText},
            date_status = #{dateStatus}
        WHERE unique_message = #{uniqueMessage}
    """)
    void updateMessage(@Param("uniqueMessage") String uniqueMessage,
                       @Param("status") String status,
                       @Param("errors") String errors,
                       @Param("messageText") String messageText,
                       @Param("dateStatus") LocalDateTime dateStatus);


    @Delete("DELETE FROM messages WHERE id = #{id}")
    void deleteMessage(Long id);


    @Select("""
                SELECT id, unique_message, group_users, template_id, file, type_file, errors, 
                       message_text, status, created_at, date_status, data::TEXT AS data
                FROM messages
                WHERE unique_message = #{uniqueMessage}
            """)
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "uniqueMessage", column = "unique_message"),
            @Result(property = "groupUsers", column = "group_users"),
            @Result(property = "templateId", column = "template_id"),
            @Result(property = "file", column = "file"),
            @Result(property = "typeFile", column = "type_file"),
            @Result(property = "errors", column = "errors"),
            @Result(property = "messageText", column = "message_text"),
            @Result(property = "status", column = "status"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "dateStatus", column = "date_status"),
            @Result(property = "data", column = "data", typeHandler = com.brnx.coreservice.config.JsonTypeHandler.class)
    })
    Message findByUniqueMessage(String uniqueMessage);
}

