package com.example.Hospital.mapper;

import com.example.Hospital.domain.Finance;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface FinanceMapper {
    List<Finance> findAll();
    int insertFinance(Finance finance);
    int changeFinance(Finance finance);
    int deleteFinance(String fid);
    Finance findByFid(String fid);

}
