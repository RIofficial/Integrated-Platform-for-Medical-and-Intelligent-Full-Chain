package com.example.Hospital.service.impl;

import com.example.Hospital.domain.Bed;
import com.example.Hospital.mapper.BedMapper;
import com.example.Hospital.service.BedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BedServiceImpl implements BedService {
    @Autowired
    private BedMapper bedMapper;

    @Override
    public List<Bed> findAll() {
        return bedMapper.findAll();
    }

    @Override
    public int insertBed(Bed bed) {
        return bedMapper.insertBed(bed);
    }

    @Override
    public int changeBed(Bed bed) {
        return bedMapper.changeBed(bed);
    }

    @Override
    public int deleteBed(String bid) {
        return bedMapper.deleteBed(bid);
    }

    @Override
    public Bed findByBid(String bid) {
        return bedMapper.findByBid(bid);
    }
}
