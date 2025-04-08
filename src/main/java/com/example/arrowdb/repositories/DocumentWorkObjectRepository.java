package com.example.arrowdb.repositories;

import com.example.arrowdb.entity.DocumentWorkObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentWorkObjectRepository extends JpaRepository<DocumentWorkObject, Integer> {

    @Query("""
            select dwo from DocumentWorkObject dwo
            where dwo.index=:index and dwo.workObject.workObjectId=:id
            """)
    List<DocumentWorkObject> findAllByIndex(int index, int id);

}