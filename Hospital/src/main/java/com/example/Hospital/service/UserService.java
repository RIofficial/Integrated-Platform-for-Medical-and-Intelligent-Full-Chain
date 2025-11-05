package com.example.Hospital.service;

import com.example.Hospital.domain.User;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface UserService {
    User login(String username);
    List<User> findAll();
    int insertUser(User user);
    int changeUser(User user);
    int deleteUser(Integer uid);

    List<Map<String, Object>> countUsersByRole();

    PageInfo<User> listUsers(int pageNum, int pageSize);
}
