package com.example.arrowdb.repositories;

import com.example.arrowdb.history.PersonalInstrumentConditionAUD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PersonalInstrumentConditionAUDRepository
        extends JpaRepository<PersonalInstrumentConditionAUD, Integer> {

    @Transactional(readOnly = true)
    @Query(value = """
            select p from PersonalInstrumentConditionAUD p
            left join fetch p.exampleRevEntity pe
            left join fetch p.employee emp
            left join fetch emp.profession
            where p.personalInstrId=:id
            order by pe.id desc
            """)
    List<PersonalInstrumentConditionAUD> findAllPersonalInstrumentConditionAUDById(Integer id);

}