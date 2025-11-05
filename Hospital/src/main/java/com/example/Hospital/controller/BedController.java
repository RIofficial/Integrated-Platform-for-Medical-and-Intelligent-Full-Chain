package com.example.Hospital.controller;

import com.example.Hospital.domain.Bed;
import com.example.Hospital.service.BedService;
import com.example.Hospital.utils.ResultInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bed")
@Tag(name="床位管理",description="床位管理相关API")
@CrossOrigin(origins = "http://localhost:5173")
public class BedController {
    @Autowired
    private BedService bedService;

    @GetMapping("/findAll")
    @Operation(summary = "获取床位列表",description = "获取床位所有信息")
    public ResultInfo<List<Bed>> findAll() {
        List<Bed> bedList = bedService.findAll();
        ResultInfo<List<Bed>> info = new ResultInfo<>();
        info.setStatus(200);
        info.setErrorMsg("查询成功");
        info.setData(bedList);
        return info;
    }

    @GetMapping("/findByBid/{bid}")
    @Operation(summary = "床号查询",description = "通过床号查询床位信息")
        public ResultInfo<Bed> findByBid(@Parameter(description = "床位床号",required = true) @PathVariable String bid) {
        Bed bed = bedService.findByBid(bid);
        ResultInfo<Bed> info = new ResultInfo<>();
        if (bed != null) {
            info.setStatus(200);
            info.setErrorMsg("查询成功");
            info.setData(bed);
        } else {
            info.setStatus(404);
            info.setErrorMsg("未找到该床位");
        }
        return info;
    }

    @PostMapping("/insertBed")
    @Operation(summary = "创建床位",description = "创建新床位")
    public ResultInfo<String> insertBed(@Parameter(description = "床位对象",required = true) @RequestBody Bed bed) {
        int rows = bedService.insertBed(bed);
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

    @PutMapping("/changeBed")
    @Operation(summary = "修改床位",description = "修改床位信息")
    public ResultInfo<String> changeBed(@Parameter(description = "床位对象",required = true) @RequestBody Bed bed) {
        int rows = bedService.changeBed(bed);
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

    @DeleteMapping("/deleteBed/{bid}")
    @Operation(summary = "删除床位",description = "通过床号删除床位信息")
    public ResultInfo<String> deleteBed(@Parameter(description = "床号",required = true) @PathVariable String bid) {
        int rows = bedService.deleteBed(bid);
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
