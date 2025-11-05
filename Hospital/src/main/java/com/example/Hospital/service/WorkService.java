package com.example.Hospital.service;

import com.example.Hospital.domain.Work;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WorkService {
    List<Work> findAll();
    int insertWork(Work work);
    int changeWork(Work work);
    int deleteWork(Integer pid);
}
