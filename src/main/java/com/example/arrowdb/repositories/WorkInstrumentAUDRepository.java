package com.example.arrowdb.repositories;

import com.example.arrowdb.history.WorkInstrumentAUD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface WorkInstrumentAUDRepository extends JpaRepository<WorkInstrumentAUD, Integer> {

    @Query(value = """
            select w from WorkInstrumentAUD w
            left join fetch w.exampleRevEntity pe
            where w.workInstrId=:id
            order by pe.id desc
            """)
    List<WorkInstrumentAUD> findAllWorkInstrumentAUDById(Integer id);

}