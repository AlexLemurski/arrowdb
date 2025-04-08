package com.example.arrowdb.repositories;

import com.example.arrowdb.history.WorkInstrumentConditionAUD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface WorkInstrumentConditionAUDRepository
        extends JpaRepository<WorkInstrumentConditionAUD, Integer> {

    @Query(value = """
            select w from WorkInstrumentConditionAUD w
            left join fetch w.exampleRevEntity pe
            left join fetch w.employee emp
            left join fetch emp.profession
            left join fetch w.workObject
            where w.workInstrId=:id
            order by pe.id desc
            """)
    List<WorkInstrumentConditionAUD> findAllWorkInstrumentConditionAUDById(Integer id);

}