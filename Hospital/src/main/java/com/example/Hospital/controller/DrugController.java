package com.example.Hospital.controller;

import com.example.Hospital.domain.Drug;
import com.example.Hospital.service.DrugService;
import com.example.Hospital.utils.ResultInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("drug")
@Tag(name="药品管理",description="药品管理相关API")
@CrossOrigin(origins = "http://localhost:5173")
public class DrugController {
    @Autowired
    private DrugService drugService;

    @GetMapping("/findAll")
    @Operation(summary = "获取药品列表",description = "获取药品所有信息")
    public ResultInfo<List<Drug>> findAll() {
        List<Drug> drugList = drugService.findAll();
        ResultInfo<List<Drug>> info = new ResultInfo<>();
        info.setStatus(200);
        info.setErrorMsg("查询成功");
        info.setData(drugList);
        return info;
    }

    @GetMapping("/findByDid/{did}")
    @Operation(summary = "药品查询",description = "通过药品编号查询药品信息")
    public ResultInfo<Drug> findByDid(@Parameter(description = "药品编号",required = true) @PathVariable String did) {
        Drug drug = drugService.findByDid(did);
        ResultInfo<Drug> info = new ResultInfo<>();
        if (drug != null) {
            info.setStatus(200);
            info.setErrorMsg("查询成功");
            info.setData(drug);
        } else {
            info.setStatus(404);
            info.setErrorMsg("未找到该药品");
        }
        return info;
    }

    @PostMapping("/insertDrug")
    @Operation(summary = "创建药品",description = "创建新药品")
    public ResultInfo<String> insertDrug(@Parameter(description = "药品对象",required = true) @RequestBody Drug drug) {
        int rows = drugService.insertDrug(drug);
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

    @PutMapping("/changeDrug")
    @Operation(summary = "修改药品",description = "修改药品信息")
    public ResultInfo<String> changeDrug(@Parameter(description = "药品对象",required = true) @RequestBody Drug drug) {
        int rows = drugService.changeDrug(drug);
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

    @DeleteMapping("/deleteDrug/{did}")
    @Operation(summary = "删除药品",description = "通过药品编号删除药品信息")
    public ResultInfo<String> deleteDrug(@PathVariable String did) {
        int rows = drugService.deleteDrug(did);
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
