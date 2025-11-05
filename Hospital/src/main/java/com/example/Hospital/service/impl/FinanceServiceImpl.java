package com.example.Hospital.service.impl;

import com.example.Hospital.domain.Finance;
import com.example.Hospital.mapper.FinanceMapper;
import com.example.Hospital.service.FinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FinanceServiceImpl implements FinanceService {
    @Autowired
    private FinanceMapper financeMapper;

    @Override
    public List<Finance> findAll() {
        return financeMapper.findAll();
    }

    @Override
    public int insertFinance(Finance finance) {
        return financeMapper.insertFinance(finance);
    }

    @Override
    public int changeFinance(Finance finance) {
        return financeMapper.changeFinance(finance);
    }

    @Override
    public int deleteFinance(String fid) {
        return financeMapper.deleteFinance(fid);
    }

    @Override
    public Finance findByFid(String fid) {
        return financeMapper.findByFid(fid);
    }

}
