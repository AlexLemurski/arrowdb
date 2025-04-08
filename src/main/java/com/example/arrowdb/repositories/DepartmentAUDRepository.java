package com.example.arrowdb.repositories;

import com.example.arrowdb.history.DepartmentAUD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DepartmentAUDRepository extends JpaRepository<DepartmentAUD, Integer> {

    @Query(value = """
            select d from DepartmentAUD as d
            left join fetch d.departmentChief
            left join fetch d.exampleRevEntity as ex
            where d.depId=:id
            order by ex.id desc
            """)
    List<DepartmentAUD> findAllDepartmentAUDById(Integer id);

}