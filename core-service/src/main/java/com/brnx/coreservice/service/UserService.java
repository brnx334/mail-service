package com.brnx.coreservice.service;

import com.brnx.coreservice.mapper.UserMapper;
import com.brnx.coreservice.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    public void createUser(User user) {
        try {
            userMapper.insertUser(user);
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to create user: " + e.getMessage(), e);
        }
    }

    public User getUserById(Long id) {
        User user = userMapper.selectUserById(id);
        if (user == null) {
            throw new IllegalArgumentException("User with ID " + id + " not found");
        }
        return user;
    }

    public void deleteUser(Long id) {
        try {
            if (userMapper.selectUserById(id) == null) {
                throw new IllegalArgumentException("User with ID " + id + " not found");
            }
            userMapper.deleteUserById(id);
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to delete user with ID " + id + ": " + e.getMessage(), e);
        }
    }

    public List<User> getAllUsersByGroupId(int groupId) {
        try {
            return userMapper.getAllUsersByGroupId(groupId);
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to retrieve users by group ID " + groupId + ": " + e.getMessage(), e);
        }
    }

    public boolean isGroupExists(int groupId){
        try {
            return userMapper.isGroupExists(groupId);
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to check is group exists with group ID - " + groupId + ": " + e.getMessage(), e);
        }
    }

    public List<String> getAllUsersEmailByGroupId(int groupId) {
        try {
            return userMapper.getEmailsByGroupId(groupId);
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to retrieve user's emails by group ID " + groupId + ": " + e.getMessage(), e);
        }
    }
}