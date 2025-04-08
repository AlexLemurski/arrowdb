package com.example.arrowdb.repositories;

import com.example.arrowdb.entity.MeasInstrument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasInstrumentRepository extends JpaRepository<MeasInstrument, Integer> {

    @Query("""
            select m.employee from MeasInstrument m
            where m.measInstrId=:id
            """)
    Integer findEmployeeIdByMeasInstId(Integer id);

}