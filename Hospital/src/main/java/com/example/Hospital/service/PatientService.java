package com.example.Hospital.service;

import com.example.Hospital.domain.Patient;
import java.util.List;
import java.util.Map;

public interface PatientService {
    List<Patient> findAll();
    int insertPatient(Patient patient);
    int changePatient(Patient patient);
    int deletePatient(String rid);
    Patient findByRid(String rid);

    List<Map<String, Object>> countPatientsByAge();
    List<Map<String, Object>> countPatientsByDisaster();
}
