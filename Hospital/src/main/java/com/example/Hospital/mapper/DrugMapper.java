package com.example.Hospital.mapper;

import com.example.Hospital.domain.Drug;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DrugMapper {
    List<Drug> findAll();
    int insertDrug(Drug drug);
    int changeDrug(Drug drug);
    int deleteDrug(String did);
    Drug findByDid(String did);
}
