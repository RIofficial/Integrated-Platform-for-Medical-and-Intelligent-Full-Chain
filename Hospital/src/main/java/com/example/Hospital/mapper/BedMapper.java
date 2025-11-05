package com.example.Hospital.mapper;

import com.example.Hospital.domain.Bed;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BedMapper {
    List<Bed> findAll();
    int insertBed(Bed bed);
    int changeBed(Bed bed);
    int deleteBed(String bid);
    Bed findByBid(String bid);
}
