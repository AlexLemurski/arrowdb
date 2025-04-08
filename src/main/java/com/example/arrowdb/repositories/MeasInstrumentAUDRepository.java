package com.example.arrowdb.repositories;

import com.example.arrowdb.history.MeasInstrumentAUD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MeasInstrumentAUDRepository extends JpaRepository<MeasInstrumentAUD, Integer> {

    @Query("""
            select m from MeasInstrumentAUD m
            where m.measInstrId=:id
            order by m.exampleRevEntity.id
            """)
    List<MeasInstrumentAUD> findAllMeasInstrumentAUDById(Integer id);

}