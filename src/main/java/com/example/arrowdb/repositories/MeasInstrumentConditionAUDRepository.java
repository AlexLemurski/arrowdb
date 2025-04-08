package com.example.arrowdb.repositories;

import com.example.arrowdb.history.MeasInstrumentConditionAUD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MeasInstrumentConditionAUDRepository
        extends JpaRepository<MeasInstrumentConditionAUD, Integer> {

    @Transactional(readOnly = true)
    @Query(value = """
            select m from MeasInstrumentConditionAUD m
            left join fetch m.exampleRevEntity pe
            left join fetch m.employee emp
            left join fetch emp.profession
            left join fetch m.workObject
            where m.measInstrId=:id
            order by pe.id desc
            """)
    List<MeasInstrumentConditionAUD> findAllMeasInstrumentConditionAUDById(Integer id);

}