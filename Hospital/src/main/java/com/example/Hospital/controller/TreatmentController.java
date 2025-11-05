package com.example.Hospital.controller;

import com.example.Hospital.domain.Treatment;
import com.example.Hospital.service.TreatmentService;
import com.example.Hospital.utils.ResultInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("treatment")
@Tag(name="医疗管理",description="医疗管理相关API")
@CrossOrigin(origins = "http://localhost:5173")
public class TreatmentController {
    @Autowired
    private TreatmentService treatmentService;

    @GetMapping("/findAll")
    @Operation(summary = "获取医疗列表",description = "获取医疗所有信息")
    public ResultInfo<List<Treatment>> findAll() {
        List<Treatment> treatmentList = treatmentService.findAll();
        ResultInfo<List<Treatment>> info = new ResultInfo<>();
        info.setStatus(200);
        info.setErrorMsg("查询成功");
        info.setData(treatmentList);
        return info;
    }

    @GetMapping("/findByTid/{tid}")
    @Operation(summary = "床号查询",description = "通过床号查询医疗信息")
    public ResultInfo<Treatment> findByTid(@Parameter(description = "医疗床号",required = true) @PathVariable String tid) {
        Treatment treatment = treatmentService.findByTid(tid);
        ResultInfo<Treatment> info = new ResultInfo<>();
        if (treatment != null) {
            info.setStatus(200);
            info.setErrorMsg("查询成功");
            info.setData(treatment);
        } else {
            info.setStatus(404);
            info.setErrorMsg("未找到该医疗记录");
        }
        return info;
    }

    @GetMapping("/countByUid")
    @Operation(summary = "统计医生医疗次数",description = "通过医生编号查询医生医疗次数")
    public ResultInfo<List<Map<String, Object>>> countByUid() {
        List<Map<String, Object>> uidCounts = treatmentService.countTreatmentsByUid();
        ResultInfo<List<Map<String, Object>>> info = new ResultInfo<>();
        info.setStatus(200);
        info.setErrorMsg("查询成功");
        info.setData(uidCounts);
        return info;
    }

    @GetMapping("/countByRid")
    @Operation(summary = "统计患者医疗次数",description = "通过患者编号查询患者医疗次数")
    public ResultInfo<List<Map<String, Object>>> countByRid() {
        List<Map<String, Object>> ridCounts = treatmentService.countTreatmentsByRid();
        ResultInfo<List<Map<String, Object>>> info = new ResultInfo<>();
        info.setStatus(200);
        info.setErrorMsg("查询成功");
        info.setData(ridCounts);
        return info;
    }

    @PostMapping("/insertTreatment")
    @Operation(summary = "创建医疗",description = "创建新医疗")
    public ResultInfo<String> insertTreatment(@Parameter(description = "医疗对象",required = true) @RequestBody Treatment treatment) {
        int rows = treatmentService.insertTreatment(treatment);
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

    @PutMapping("/changeTreatment")
    @Operation(summary = "修改医疗",description = "修改医疗信息")
    public ResultInfo<String> changeTreatment(@Parameter(description = "医疗对象",required = true) @RequestBody Treatment treatment) {
        int rows = treatmentService.changeTreatment(treatment);
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

    @DeleteMapping("/deleteTreatment/{tid}")
    @Operation(summary = "删除医疗",description = "通过床号删除医疗信息")
    public ResultInfo<String> deleteTreatment(@Parameter(description = "床号",required = true) @PathVariable String tid) {
        int rows = treatmentService.deleteTreatment(tid);
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
