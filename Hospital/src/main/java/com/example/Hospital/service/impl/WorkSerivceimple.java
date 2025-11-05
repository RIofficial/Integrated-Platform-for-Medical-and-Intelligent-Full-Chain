package com.example.Hospital.service.impl;

import com.example.Hospital.domain.Work;
import com.example.Hospital.mapper.WorkMapper;
import com.example.Hospital.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException; // 导入 TransactionException

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class WorkSerivceimple implements WorkService{
    @Autowired
    private WorkMapper workMapper;

    @Autowired
    private BlockchainService blockchainService; // 注入 BlockchainService

    // 移除 ObjectMapper，不再需要将 Work 对象转 JSON 字符串

    @Override
    public List<Work> findAll() {
        return workMapper.findAll();
    }

    public int insertWork(Work work) {
        int result = workMapper.insertWork(work);
        if (result > 0) {
            try {
                // 直接上链，不再计算哈希
                TransactionReceipt receipt = blockchainService.setWorkHash(work.getPid()); 
                System.out.println("排班记录 (PID: " + work.getPid() + ") 已上链，交易哈希: " + receipt.getTransactionHash());
            } catch (IOException | ExecutionException | InterruptedException | TransactionException e) { 
                System.err.println("排班记录 (PID: " + work.getPid() + ") 上链失败: " + e.getMessage());
                // 实际项目中可能需要更细致的错误处理，例如记录日志，回滚数据库事务等
            }
        }
        return result;
    }

    public int changeWork(Work work) {
        int result = workMapper.changeWork(work);
        if (result > 0) {
            try {
                // 直接上链，不再计算哈希
                TransactionReceipt receipt = blockchainService.setWorkHash(work.getPid());
                System.out.println("排班记录 (PID: " + work.getPid() + ") 已更新并上链，交易哈希: " + receipt.getTransactionHash());
            } catch (IOException | ExecutionException | InterruptedException | TransactionException e) {
                System.err.println("排班记录 (PID: " + work.getPid() + ") 更新上链失败: " + e.getMessage());
                // 实际项目中可能需要更细致的错误处理，例如记录日志，回滚数据库事务等
            }
        }
        return result;
    }

    public int deleteWork(Integer pid) {
        return workMapper.deleteWork(pid);
    }
}
