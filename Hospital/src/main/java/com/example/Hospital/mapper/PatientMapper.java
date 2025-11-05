package com.example.Hospital.mapper;

import com.example.Hospital.domain.Patient;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PatientMapper {
    List<Patient> findAll();
    int insertPatient(Patient patient);
    int changePatient(Patient patient);
    int deletePatient(String rid);
    Patient findByRid(String rid);

    List<Map<String, Object>> countPatientsByAge(); // 新增方法：统计患者年龄分布
    List<Map<String, Object>> countPatientsByDisaster(); // 新增方法：统计患者病情分布
}
