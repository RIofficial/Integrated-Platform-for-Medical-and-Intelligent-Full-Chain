package com.example.Hospital.service.impl;

import com.example.Hospital.domain.Drug;
import com.example.Hospital.mapper.DrugMapper;
import com.example.Hospital.service.DrugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DrugServiceImpl implements DrugService {
    @Autowired
    private DrugMapper drugMapper;

    @Override
    public List<Drug> findAll() {
        return drugMapper.findAll();
    }

    @Override
    public int insertDrug(Drug drug) {
        return drugMapper.insertDrug(drug);
    }

    @Override
    public int changeDrug(Drug drug) {
        return drugMapper.changeDrug(drug);
    }

    @Override
    public int deleteDrug(String did) {
        return drugMapper.deleteDrug(did);
    }

    @Override
    public Drug findByDid(String did) {
        return drugMapper.findByDid(did);
    }
}
