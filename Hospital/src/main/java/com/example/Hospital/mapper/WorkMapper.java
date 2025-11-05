package com.example.Hospital.mapper;

import com.example.Hospital.domain.Work;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WorkMapper {
    List<Work> findAll();
    int insertWork(Work work);
    int changeWork(Work work);
    int deleteWork(Integer uid);
}
