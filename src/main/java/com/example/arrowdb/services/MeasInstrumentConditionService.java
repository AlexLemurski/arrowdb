package com.example.arrowdb.services;

import com.example.arrowdb.entity.MeasInstrumentCondition;

import java.util.List;

public interface MeasInstrumentConditionService {

    List<MeasInstrumentCondition> findMeasInstrumentConditions();
    MeasInstrumentCondition findMeasInstrumentConditionById(Integer id);
    void saveMeasInstrumentCondition(MeasInstrumentCondition instruments);
    void deleteMeasInstrumentConditionById(Integer id);

}