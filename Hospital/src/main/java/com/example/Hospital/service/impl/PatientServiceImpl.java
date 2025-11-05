package com.example.Hospital.service.impl;

import com.example.Hospital.domain.Patient;
import com.example.Hospital.mapper.PatientMapper;
import com.example.Hospital.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PatientServiceImpl implements PatientService {
    @Autowired
    private PatientMapper patientMapper;

    @Override
    public List<Patient> findAll() {
        return patientMapper.findAll();
    }

    @Override
    public int insertPatient(Patient patient) {
        return patientMapper.insertPatient(patient);
    }

    @Override
    public int changePatient(Patient patient) {
        return patientMapper.changePatient(patient);
    }

    @Override
    public int deletePatient(String rid) {
        return patientMapper.deletePatient(rid);
    }

    @Override
    public Patient findByRid(String rid) {
        return patientMapper.findByRid(rid);
    }

    @Override
    public List<Map<String, Object>> countPatientsByAge() {
        return patientMapper.countPatientsByAge();
    }

    @Override
    public List<Map<String, Object>> countPatientsByDisaster() {
        return patientMapper.countPatientsByDisaster();
    }
}
