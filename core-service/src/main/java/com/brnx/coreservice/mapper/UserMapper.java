package com.brnx.coreservice.mapper;

import com.brnx.coreservice.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
@Mapper
@Repository
public interface UserMapper {

    @Insert("INSERT INTO users (name, group_id, email) VALUES (#{name}, #{groupId}, #{email})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insertUser(User user);

    @Select("SELECT id, name, group_id, email FROM users WHERE id = #{id}")
    User selectUserById(Long id);

    @Delete("DELETE FROM users WHERE id = #{id}")
    void deleteUserById(Long id);

    @Select("SELECT id, name, group_id, email FROM users WHERE group_id = #{groupId}")
    List<User> getAllUsersByGroupId(int groupId);

    @Select("SELECT email FROM users WHERE group_id = #{groupId}")
    List<String> getEmailsByGroupId(int groupId);

    @Select("SELECT EXISTS (SELECT 1 FROM users WHERE group_id = #{groupId})")
    boolean isGroupExists(int groupId);

}
