package com.example.arrowdb.repositories;

import com.example.arrowdb.history.ProfessionAUD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProfessionAUDRepository extends JpaRepository<ProfessionAUD, Integer> {

    @Transactional(readOnly = true)
    @Query(value = """
            select p from ProfessionAUD p
            left join fetch p.exampleRevEntity as ex
            where p.profId=:id
            order by ex.id desc
            """)
    List<ProfessionAUD> findAllProfessionAUDById(Integer id);

}