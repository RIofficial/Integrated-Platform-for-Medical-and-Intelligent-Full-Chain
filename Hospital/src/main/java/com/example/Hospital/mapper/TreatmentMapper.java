package com.example.Hospital.mapper;

import com.example.Hospital.domain.Treatment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TreatmentMapper {
    List<Treatment> findAll();
    int insertTreatment(Treatment treatment);
    int changeTreatment(Treatment treatment);
    int deleteTreatment(String tid);
    Treatment findByTid(String tid);

    List<Map<String, Object>> countTreatmentsByUid(); // 新增方法：按医生统计治疗次数
    List<Map<String, Object>> countTreatmentsByRid(); // 新增方法：按患者统计治疗次数
}
