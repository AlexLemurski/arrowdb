package com.example.arrowdb.repositories;

import com.example.arrowdb.history.WorkObjectAUD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface WorkObjectAUDRepository extends JpaRepository<WorkObjectAUD, Integer> {

    @Query("""
            select w from WorkObjectAUD w
            where w.workObjectId=:id
            order by w.exampleRevEntity.id desc
            """)
    List<WorkObjectAUD> findAllWorkObjectAUDById(Integer id);
}
