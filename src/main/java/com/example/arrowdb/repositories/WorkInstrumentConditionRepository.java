package com.example.arrowdb.repositories;

import com.example.arrowdb.entity.WorkInstrumentCondition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkInstrumentConditionRepository
        extends JpaRepository<WorkInstrumentCondition, Integer> {

}