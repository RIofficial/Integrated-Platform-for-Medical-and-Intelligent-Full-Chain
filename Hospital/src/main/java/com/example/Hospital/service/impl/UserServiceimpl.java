package com.example.Hospital.service.impl;

import com.example.Hospital.mapper.UserMapper;
import com.example.Hospital.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.Hospital.domain.User;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceimpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public User login(String username)
    {
        return userMapper.findByUsername(username);
    }
    public List<User> findAll(){
        return userMapper.findAll();
    }
    public int insertUser(User user){
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空");
        }
        if (user.getPassword() == null || user.getPassword().length() < 6) {
            throw new IllegalArgumentException("密码长度不能少于6位");
        }
        return userMapper.insertUser(user);
    }
    public int changeUser(User user) {
        return userMapper.changeUser(user);
    }
    public int deleteUser(Integer uid){
        return userMapper.deleteUser(uid);
    }

    @Override
    public List<Map<String, Object>> countUsersByRole() {
        return userMapper.countUsersByRole();
    }

    public PageInfo<User> listUsers(int pageNum, int pageSize) {

        PageHelper.startPage(pageNum, pageSize);

        List<User> users = userMapper.findUsersByPage();

        return new PageInfo<>(users);
    }

}
