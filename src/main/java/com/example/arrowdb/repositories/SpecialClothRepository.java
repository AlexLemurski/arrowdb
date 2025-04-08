package com.example.arrowdb.repositories;

import com.example.arrowdb.entity.SpecialCloth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpecialClothRepository extends JpaRepository<SpecialCloth, Integer> {

    @Override
    @Query("""
            select sc from SpecialCloth sc
            left join fetch sc.specialClothEmployeeList
            order by sc.specClothId
            """)
    List<SpecialCloth> findAll();

    @Override
    @Query("""
            select sc from SpecialCloth sc
            left join fetch sc.specialClothEmployeeList sce
            left join fetch sce.employee scep
            left join fetch scep.profession
            where sc.specClothId=:id
            """)
    Optional<SpecialCloth> findById(Integer id);

}
