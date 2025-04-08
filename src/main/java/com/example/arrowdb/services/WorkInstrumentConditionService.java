package com.example.arrowdb.services;

import com.example.arrowdb.entity.WorkInstrumentCondition;

import java.util.List;

public interface WorkInstrumentConditionService {

    List<WorkInstrumentCondition> findWorkInstrumentConditions();
    WorkInstrumentCondition findWorkInstrumentConditionById(Integer id);
    void saveWorkInstrumentCondition(WorkInstrumentCondition instruments);
    void deleteWorkInstrumentConditionById(Integer id);

}