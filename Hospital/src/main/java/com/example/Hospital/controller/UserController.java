package com.example.Hospital.controller;

import com.example.Hospital.service.UserService;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.Hospital.domain.User;
import com.example.Hospital.utils.ResultInfo;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("user")
@Tag(name="用户管理",description="用户管理相关API")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @Operation(summary = "登录",description = "通过账号密码登录")
    public ResultInfo<User> login(@Parameter(description = "账号",required = true) String username,@Parameter(description = "密码",required = true) String password) {
        User loginUser = userService.login(username);
        ResultInfo<User> info = new ResultInfo<>();
        if(loginUser == null)
        {
            info.setStatus(500);
            info.setErrorMsg("账号名错误");
        }
        else if(!loginUser.getPassword().equals(password)) {
            info.setStatus(500);
            info.setErrorMsg("密码错误");
        }
        else {
            info.setStatus(200);
            loginUser.setPassword(null);
            info.setData(loginUser);
        }
        return info;
    }

    @GetMapping("/findAll")
    @Operation(summary = "获取用户列表",description = "获取用户所有信息")
    public ResultInfo<List<User>> findAll() {
        List<User> userList = userService.findAll();
        ResultInfo<List<User>> info = new ResultInfo<>();
        info.setStatus(200);
        info.setErrorMsg("查询成功");
        info.setData(userList);
        return info;
    }

    @GetMapping("/countByRole")
    @Operation(summary = "职责统计",description = "统计每类职责数量")
    public ResultInfo<List<Map<String, Object>>> countByRole() {
        List<Map<String, Object>> roleCounts = userService.countUsersByRole();
        ResultInfo<List<Map<String, Object>>> info = new ResultInfo<>();
        info.setStatus(200);
        info.setErrorMsg("查询成功");
        info.setData(roleCounts);
        return info;
    }

    @PostMapping("/insertUser")
    @Operation(summary = "创建用户",description = "创建新用户")
    public ResultInfo<String> insertUser(@Parameter(description = "用户对象",required = true) @RequestBody User user) {
        int rows = userService.insertUser(user);
        ResultInfo info = new ResultInfo<>();
        if (rows > 0) {
            info.setErrorMsg("成功！");
            info.setStatus(200);
            return info;
        } else {
            info.setErrorMsg("失败");
            info.setStatus(500);
            return info;
        }
    }
    @PutMapping("/changeUser")
    @Operation(summary = "修改用户",description = "修改用户信息")
    public ResultInfo<String> changeUser(@Parameter(description = "用户对象",required = true) @RequestBody User user) {
        int rows = userService.changeUser(user);
        ResultInfo info = new ResultInfo<>();
        if (rows > 0) {
            info.setErrorMsg("成功！");
            info.setStatus(200);
            return info;
        } else {
            info.setErrorMsg("失败");
            info.setStatus(500);
            return info;
        }
    }

    @DeleteMapping("/deleteUser/{uid}")
    @Operation(summary = "删除用户",description = "通过床号删除用户信息")
    public ResultInfo<String> deleteUser(@Parameter(description = "床号",required = true) @PathVariable Integer uid) {
        int rows = userService.deleteUser(uid);
        ResultInfo info = new ResultInfo<>();
        if (rows > 0) {
            info.setErrorMsg("成功！");
            info.setStatus(200);
            return info;
        } else {
            info.setErrorMsg("失败");
            info.setStatus(500);
            return info;
        }
    }

    @GetMapping("/divide")
    @Operation(summary = "分页",description = "通过pagehelper进行分页")
    public ResponseEntity<ResultInfo<PageInfo<User>>> getUsers(
            @Parameter(description = "页码",required = true) @RequestParam(defaultValue = "1") int pageNum,
            @Parameter(description = "页数",required = true) @RequestParam(defaultValue = "5") int pageSize
    ) {
        PageInfo<User> pageInfo = userService.listUsers(pageNum, pageSize);
        ResultInfo<PageInfo<User>> info = new ResultInfo<>();
        if (pageInfo != null) {
            info.setErrorMsg("成功！");
            info.setStatus(200);
            info.setData(pageInfo);
            return ResponseEntity.ok(info);
        } else {
            info.setStatus(500);
            info.setErrorMsg("查询失败");
            return ResponseEntity.status(500).body(info);
        }
    }
}

