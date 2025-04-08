package com.example.arrowdb.repositories;

import com.example.arrowdb.entity.PersonalInstrumentCondition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalInstrumentConditionRepository
        extends JpaRepository<PersonalInstrumentCondition, Integer> {

}