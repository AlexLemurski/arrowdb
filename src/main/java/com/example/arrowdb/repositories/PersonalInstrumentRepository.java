package com.example.arrowdb.repositories;

import com.example.arrowdb.entity.PersonalInstrument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonalInstrumentRepository extends JpaRepository<PersonalInstrument, Integer> {

    @Query("""
            select p from PersonalInstrument p
            left join fetch p.employee emp
            left join fetch emp.profession
            order by p.personalInstrId
            """)
    List<PersonalInstrument> findPersonalInstrumentForCatalogueMenu();

    @Override
    @Query("""
            select p from PersonalInstrument p
            left join fetch p.employee emp
            left join fetch emp.profession
            where p.personalInstrId=:id
            """)
    Optional<PersonalInstrument> findById(Integer id);

    @Query("""
            select employee.empId from PersonalInstrument
            where personalInstrId=:id""")
    Integer findPersonalInstIdByEmployeeId(Integer id);

    @Query("""
            select p from PersonalInstrument p
            where p.technicalConditionENUM = 0 and p.personalConditionENUM = 1
            order by p.personalInstrId
            """)
    List<PersonalInstrument> findAllActiveAndFreePersonalInstrument();

}