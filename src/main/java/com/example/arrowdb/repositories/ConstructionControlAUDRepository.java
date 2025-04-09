package com.example.arrowdb.repositories;

import com.example.arrowdb.history.ConstructionControlAUD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ConstructionControlAUDRepository extends JpaRepository<ConstructionControlAUD, Integer>{

    @Query("""
            select cc from ConstructionControlAUD cc
            left join fetch cc.responsibleFromContractor
            left join fetch cc.responsibleFromContractor
            left join fetch cc.workObject
            where cc.constrControlId=:id
            order by cc.exampleRevEntity.id desc
            """)
    List<ConstructionControlAUD> findAllConstructionControlsAUDById(Integer id);

}