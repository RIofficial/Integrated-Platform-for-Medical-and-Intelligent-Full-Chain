package com.example.Hospital.controller;

import com.example.Hospital.domain.Work;
import com.example.Hospital.service.WorkService;
import com.example.Hospital.utils.ResultInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("work")
@Tag(name="排班管理",description="排班管理相关API")
@CrossOrigin(origins = "http://localhost:5173")
public class WorkController {
    @Autowired
    private WorkService workService;
    @GetMapping("/findAll")
    @Operation(summary = "获取排班列表",description = "获取排班所有信息")
    public ResultInfo<List<Work>> findAll() {
        List<Work> WorkList = workService.findAll();
        ResultInfo<List<Work>> info = new ResultInfo<>();
        info.setStatus(200);
        info.setErrorMsg("查询成功");
        info.setData(WorkList);
        return info;
    }

    @PostMapping("/insertWork")
    @Operation(summary = "创建排班",description = "创建新排班")
    public ResultInfo<String> insertWork(@Parameter(description = "排班对象",required = true) @RequestBody Work work) {
        int rows = workService.insertWork(work);
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
    @PutMapping("/changeWork")
    @Operation(summary = "修改排班",description = "修改排班信息")
    public ResultInfo<String> changeWork(@Parameter(description = "排班对象",required = true) @RequestBody Work work) {
        int rows = workService.changeWork(work);
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

    @DeleteMapping("/deleteWork/{pid}")
    @Operation(summary = "删除排班",description = "通过床号删除排班信息")
    public ResultInfo<String> deleteWork(@Parameter(description = "床号",required = true) @PathVariable Integer pid) {
        int rows = workService.deleteWork(pid);
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
}
