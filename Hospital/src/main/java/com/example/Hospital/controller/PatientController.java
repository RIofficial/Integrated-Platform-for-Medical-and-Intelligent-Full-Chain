package com.example.Hospital.controller;

import com.example.Hospital.domain.Patient;
import com.example.Hospital.service.PatientService;
import com.example.Hospital.utils.ResultInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("patient")
@Tag(name="患者管理",description="患者管理相关API")
@CrossOrigin(origins = "http://localhost:5173")
public class PatientController {
    @Autowired
    private PatientService patientService;

    @GetMapping("/findAll")
    @Operation(summary = "获取患者列表",description = "获取患者所有信息")
    public ResultInfo<List<Patient>> findAll() {
        List<Patient> patientList = patientService.findAll();
        ResultInfo<List<Patient>> info = new ResultInfo<>();
        info.setStatus(200);
        info.setErrorMsg("查询成功");
        info.setData(patientList);
        return info;
    }

    @GetMapping("/findByRid/{rid}")
    @Operation(summary = "床号查询",description = "通过床号查询患者信息")
    public ResultInfo<Patient> findByRid(@Parameter(description = "患者床号",required = true) @PathVariable String rid) {
        Patient patient = patientService.findByRid(rid);
        ResultInfo<Patient> info = new ResultInfo<>();
        if (patient != null) {
            info.setStatus(200);
            info.setErrorMsg("查询成功");
            info.setData(patient);
        } else {
            info.setStatus(404);
            info.setErrorMsg("未找到该患者");
        }
        return info;
    }

    @GetMapping("/countByAge")
    @Operation(summary = "统计年龄",description = "通过年龄统计病人数量")
    public ResultInfo<List<Map<String, Object>>> countByAge() {
        List<Map<String, Object>> ageCounts = patientService.countPatientsByAge();
        ResultInfo<List<Map<String, Object>>> info = new ResultInfo<>();
        info.setStatus(200);
        info.setErrorMsg("查询成功");
        info.setData(ageCounts);
        return info;
    }

    @GetMapping("/countByDisaster")
    @Operation(summary = "统计疾病",description = "通过疾病统计病人数量")
    public ResultInfo<List<Map<String, Object>>> countByDisaster() {
        List<Map<String, Object>> disasterCounts = patientService.countPatientsByDisaster();
        ResultInfo<List<Map<String, Object>>> info = new ResultInfo<>();
        info.setStatus(200);
        info.setErrorMsg("查询成功");
        info.setData(disasterCounts);
        return info;
    }

    @PostMapping("/insertPatient")
    @Operation(summary = "创建患者",description = "创建新患者")
    public ResultInfo<String> insertPatient(@Parameter(description = "患者对象",required = true) @RequestBody Patient patient) {
        int rows = patientService.insertPatient(patient);
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

    @PutMapping("/changePatient")
    @Operation(summary = "修改患者",description = "修改患者信息")
    public ResultInfo<String> changePatient(@Parameter(description = "患者对象",required = true) @RequestBody Patient patient) {
        int rows = patientService.changePatient(patient);
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

    @DeleteMapping("/deletePatient/{rid}")
    @Operation(summary = "删除患者",description = "通过床号删除患者信息")
    public ResultInfo<String> deletePatient(@Parameter(description = "床号",required = true) @PathVariable String rid) {
        int rows = patientService.deletePatient(rid);
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
