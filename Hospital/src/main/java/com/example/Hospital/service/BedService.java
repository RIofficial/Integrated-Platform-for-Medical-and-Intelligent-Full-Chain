package com.example.Hospital.service;

import com.example.Hospital.domain.Bed;
import java.util.List;

public interface BedService {
    List<Bed> findAll();
    int insertBed(Bed bed);
    int changeBed(Bed bed);
    int deleteBed(String bid);
    Bed findByBid(String bid);
}
