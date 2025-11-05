package com.example.Hospital.service;

import com.example.Hospital.domain.Finance;
import java.util.List;
import java.util.Map;

public interface FinanceService {
    List<Finance> findAll();
    int insertFinance(Finance finance);
    int changeFinance(Finance finance);
    int deleteFinance(String fid);
    Finance findByFid(String fid);

}
