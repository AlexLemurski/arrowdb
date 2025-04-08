package com.example.arrowdb.repositories;

import com.example.arrowdb.entity.SpecialClothEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecialClothEmployeeRepository extends JpaRepository<SpecialClothEmployee, Integer> {

    @Query("""
            select s from SpecialClothEmployee s
            where s.empId=:id
            """)
    List<SpecialClothEmployee> findAllSpecialClothEmployeeByEmployeeId(Integer id);

    @Query("""
            select s.empId from SpecialClothEmployee s
            where s.specialCloth.specClothId=:id
            """)
    List<Integer> findAllEmployeeBySpecialClothEmployeeId(Integer id);

    @Query("""
            select s.empId from SpecialClothEmployee s
            where s.specClothEmpId=:id
            """)
    Integer findEmployeeIdBySpecialClothEmployeeId(Integer id);

    @Query("""
            select s.specialCloth.specClothId from SpecialClothEmployee s
            where s.empId=:id
            """)
    List<Integer> findSpecialClothByEmployeeId (Integer id);

}