package com.example.arrowdb.repositories;

import com.example.arrowdb.entity.Vocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VocationRepository extends JpaRepository<Vocation, Integer> {

    @Query("""
            select v from Vocation v
            where v.employee.empId=:id
            """)
    List<Vocation> findByVocationsByEmployeeId(Integer id);

    @Query("""
            select v.employee.empId from Vocation v
            where v.vocId=:id
            """)
    Integer findEmployeeByVocationId(int id);

    @Query("""
            select v from Vocation v
            where current_date between v.startOfVocation
            and v.endOfVocation
            and v.employee.empId=:id
            """)
    List<Vocation> daysOfVocations(int id);

}