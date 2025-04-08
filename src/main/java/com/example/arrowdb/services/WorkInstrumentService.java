package com.example.arrowdb.services;

import com.example.arrowdb.entity.WorkInstrument;

import java.util.List;

public interface WorkInstrumentService {

    List<WorkInstrument> findAllWorkInstruments();
    WorkInstrument findWorkInstrumentById(Integer id);
    void saveWorkInstrument(WorkInstrument instruments);
    void deleteWorkInstrumentsById(Integer id);
    Integer findEmployeeIdByWorkInstId(Integer id);

}
