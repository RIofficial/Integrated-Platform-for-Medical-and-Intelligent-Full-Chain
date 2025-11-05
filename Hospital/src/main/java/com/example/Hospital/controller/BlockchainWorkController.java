package com.example.Hospital.controller;

import com.example.Hospital.domain.Work;
import com.example.Hospital.service.impl.BlockchainService;
import com.example.Hospital.service.WorkService;
import com.example.Hospital.utils.ResultInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/blockchain-work")
@Tag(name="区块链排班",description="通过区块链进行排班查询")
@CrossOrigin(origins = "http://localhost:5173")
public class BlockchainWorkController {

    @Autowired
    private WorkService workService;

    @Autowired
    private BlockchainService blockchainService;

    @Operation(summary = "获取排班信息",description = "通过区块链进行排班信息查询")
    @GetMapping("/records")
    public ResultInfo<List<Map<String, Object>>> getAllWorkRecordsWithBlockchainInfo() {
        List<Work> workRecords = workService.findAll();
        List<Map<String, Object>> resultList = new ArrayList<>();

        for (Work work : workRecords) {
            Map<String, Object> recordInfo = new HashMap<>();
            recordInfo.put("pid", work.getPid());
            recordInfo.put("username", work.getUsername());
            recordInfo.put("date", work.getDate());
            recordInfo.put("endtime", work.getEndtime());
            recordInfo.put("position", work.getPosition());

            try {
                String blockchainTxHash = blockchainService.getWorkHash(work.getPid());
                recordInfo.put("blockchainTxHash", blockchainTxHash != null && !blockchainTxHash.isEmpty() ? blockchainTxHash : "未上链");


            } catch (IOException | ExecutionException | InterruptedException e) {
                System.err.println("查询区块链交易哈希失败 (PID: " + work.getPid() + "): " + e.getMessage());
                recordInfo.put("blockchainTxHash", "查询失败");
            }
            resultList.add(recordInfo);
        }

        return ResultInfo.success(resultList);
    }
}
