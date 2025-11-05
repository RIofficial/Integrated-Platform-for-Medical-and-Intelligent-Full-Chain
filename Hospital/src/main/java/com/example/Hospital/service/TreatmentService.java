package com.example.Hospital.service;

import com.example.Hospital.domain.Treatment;
import java.util.List;
import java.util.Map;

public interface TreatmentService {
    List<Treatment> findAll();
    int insertTreatment(Treatment treatment);
    int changeTreatment(Treatment treatment);
    int deleteTreatment(String tid);
    Treatment findByTid(String tid);

    List<Map<String, Object>> countTreatmentsByUid();
    List<Map<String, Object>> countTreatmentsByRid();
}
