package com.example.arrowdb.repositories;

import com.example.arrowdb.history.PersonalInstrumentAUD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PersonalInstrumentAUDRepository extends JpaRepository<PersonalInstrumentAUD, Integer> {

    @Transactional(readOnly = true)
    @Query(value = """
            select p from PersonalInstrumentAUD p
            left join fetch p.exampleRevEntity pe
            where p.personalInstrId=:id
            order by pe.id desc
            """)
    List<PersonalInstrumentAUD> findAllPersonalInstrumentAUDById(Integer id);

}