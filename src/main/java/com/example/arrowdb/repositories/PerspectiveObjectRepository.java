package com.example.arrowdb.repositories;

import com.example.arrowdb.entity.PerspectiveObject;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PerspectiveObjectRepository extends JpaRepository<PerspectiveObject, Integer> {

    @Override
    @Query("""
            select p from PerspectiveObject p
            left join fetch p.employeeEquipmentList
            left join fetch p.employeeWorkList
            left join fetch p.employeeObserverList
            left join fetch p.documentPerspectiveList
            where p.perspectiveId=:id
            """)
    Optional<PerspectiveObject> findById(@NotNull Integer id);

    @Override
    @Query("""
            select p from PerspectiveObject p
            left join fetch p.employeeObserverList
            left join fetch p.employeeEquipmentList
            left join fetch p.employeeWorkList
            order by p.perspectiveId
            """)
    List<PerspectiveObject> findAll();
    
}