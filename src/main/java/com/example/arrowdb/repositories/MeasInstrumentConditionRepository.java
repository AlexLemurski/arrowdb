package com.example.arrowdb.repositories;

import com.example.arrowdb.entity.MeasInstrumentCondition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeasInstrumentConditionRepository
        extends JpaRepository<MeasInstrumentCondition, Integer> {

}