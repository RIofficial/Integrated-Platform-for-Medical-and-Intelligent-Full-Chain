package com.example.Hospital.service;

import com.example.Hospital.domain.Drug;
import java.util.List;

public interface DrugService {
    List<Drug> findAll();
    int insertDrug(Drug drug);
    int changeDrug(Drug drug);
    int deleteDrug(String did);
    Drug findByDid(String did);
}
