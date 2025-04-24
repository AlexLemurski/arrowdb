package com.example.arrowdb.repositories;

import com.example.arrowdb.entity.MeasInstrumentCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasInstrumentConditionRepository
        extends JpaRepository<MeasInstrumentCondition, Integer> {

}