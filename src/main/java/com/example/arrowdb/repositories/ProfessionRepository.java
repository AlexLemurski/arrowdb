package com.example.arrowdb.repositories;

import com.example.arrowdb.entity.Profession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfessionRepository extends JpaRepository<Profession, Integer> {

    @Query("""
            select p from Profession p
            left join fetch p.employeeList
            left join fetch p.documentProfessionList
            where p.profId=:id
            """)
    Profession findProfessionByIdForView(int id);

    @Query("""
            select p from Profession p
            left join fetch p.employeeList
            order by p.profId
            """)
    List<Profession> findAllProfessionsForMainMenu();

    @Query("""
            select p from Profession p
            where p.profAndDepStatusENUM = 0
            order by p.profId
            """)
    List<Profession> findAllActiveProfessions();

    @Override
    @Query("""
            select p from Profession p
            left join fetch p.employeeList e
            order by p.profId
            """)
    List<Profession> findAll();

}