package com.example.arrowdb.repositories;

import com.example.arrowdb.entity.WorkInstrument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkInstrumentRepository extends JpaRepository<WorkInstrument, Integer> {

    @Query("""
            select w.employee from WorkInstrument w
            where w.workInstrId=:id
            """)
    Integer findEmployeeIdByWorkInstId(Integer id);

}