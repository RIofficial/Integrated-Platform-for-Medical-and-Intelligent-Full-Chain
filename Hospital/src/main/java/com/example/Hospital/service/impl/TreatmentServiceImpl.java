package com.example.Hospital.service.impl;

import com.example.Hospital.domain.Treatment;
import com.example.Hospital.mapper.TreatmentMapper;
import com.example.Hospital.service.TreatmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TreatmentServiceImpl implements TreatmentService {
    @Autowired
    private TreatmentMapper treatmentMapper;

    @Override
    public List<Treatment> findAll() {
        return treatmentMapper.findAll();
    }

    @Override
    public int insertTreatment(Treatment treatment) {
        return treatmentMapper.insertTreatment(treatment);
    }

    @Override
    public int changeTreatment(Treatment treatment) {
        return treatmentMapper.changeTreatment(treatment);
    }

    @Override
    public int deleteTreatment(String tid) {
        return treatmentMapper.deleteTreatment(tid);
    }

    @Override
    public Treatment findByTid(String tid) {
        return treatmentMapper.findByTid(tid);
    }

    @Override
    public List<Map<String, Object>> countTreatmentsByUid() {
        return treatmentMapper.countTreatmentsByUid();
    }

    @Override
    public List<Map<String, Object>> countTreatmentsByRid() {
        return treatmentMapper.countTreatmentsByRid();
    }
}
