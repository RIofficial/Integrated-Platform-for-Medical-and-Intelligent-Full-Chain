package com.example.Hospital.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.example.Hospital.domain.User;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {
    User findByUsername(String username);
    List<User> findAll();
    int insertUser(User user);
    int changeUser(User user);
    int deleteUser(Integer uid);

    List<Map<String, Object>> countUsersByRole();

    List<User> findUsersByPage();
}
