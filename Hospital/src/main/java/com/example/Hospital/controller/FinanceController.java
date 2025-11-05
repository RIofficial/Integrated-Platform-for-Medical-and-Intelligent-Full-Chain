package com.example.Hospital.controller;

import com.example.Hospital.domain.Finance;
import com.example.Hospital.service.FinanceService;
import com.example.Hospital.utils.ResultInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("finance")
@Tag(name="财务管理",description="财务管理相关API")
@CrossOrigin(origins = "http://localhost:5173")
public class FinanceController {
    @Autowired
    private FinanceService financeService;

    @GetMapping("/findAll")
    @Operation(summary = "获取财务列表",description = "获取财务所有信息")
    public ResultInfo<List<Finance>> findAll() {
        List<Finance> financeList = financeService.findAll();
        ResultInfo<List<Finance>> info = new ResultInfo<>();
        info.setStatus(200);
        info.setErrorMsg("查询成功");
        info.setData(financeList);
        return info;
    }

    @GetMapping("/findByFid/{fid}")
    @Operation(summary = "床号查询",description = "通过床号查询财务信息")
    public ResultInfo<Finance> findByFid(@Parameter(description = "财务床号",required = true) @PathVariable String fid) {
        Finance finance = financeService.findByFid(fid);
        ResultInfo<Finance> info = new ResultInfo<>();
        if (finance != null) {
            info.setStatus(200);
            info.setErrorMsg("查询成功");
            info.setData(finance);
        } else {
            info.setStatus(404);
            info.setErrorMsg("未找到该财务记录");
        }
        return info;
    }

    @PostMapping("/insertFinance")
    @Operation(summary = "创建财务",description = "创建新财务")
    public ResultInfo<String> insertFinance(@Parameter(description = "财务对象",required = true) @RequestBody Finance finance) {
        int rows = financeService.insertFinance(finance);
        ResultInfo<String> info = new ResultInfo<>();
        if (rows > 0) {
            info.setErrorMsg("新增成功！");
            info.setStatus(200);
        } else {
            info.setErrorMsg("新增失败！");
            info.setStatus(500);
        }
        return info;
    }
    @Operation(summary = "修改财务",description = "修改财务信息")
    @PutMapping("/changeFinance")
    public ResultInfo<String> changeFinance(@Parameter(description = "财务对象",required = true) @RequestBody Finance finance) {
        int rows = financeService.changeFinance(finance);
        ResultInfo<String> info = new ResultInfo<>();
        if (rows > 0) {
            info.setErrorMsg("修改成功！");
            info.setStatus(200);
        } else {
            info.setErrorMsg("修改失败！");
            info.setStatus(500);
        }
        return info;
    }

    @DeleteMapping("/deleteFinance/{fid}")
    @Operation(summary = "删除财务",description = "通过床号删除财务信息")
    public ResultInfo<String> deleteFinance(@Parameter(description = "床号",required = true) @PathVariable String fid) {
        int rows = financeService.deleteFinance(fid);
        ResultInfo<String> info = new ResultInfo<>();
        if (rows > 0) {
            info.setErrorMsg("删除成功！");
            info.setStatus(200);
        } else {
            info.setErrorMsg("删除失败！");
            info.setStatus(500);
        }
        return info;
    }
}
