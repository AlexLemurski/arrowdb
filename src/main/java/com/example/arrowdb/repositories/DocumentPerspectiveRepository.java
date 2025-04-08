package com.example.arrowdb.repositories;

import com.example.arrowdb.entity.DocumentPerspective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentPerspectiveRepository extends JpaRepository<DocumentPerspective, Integer> {

    @Query("""
            select dp from DocumentPerspective  dp
            where dp.index=:index and dp.perspectiveObject.perspectiveId=:id
            """)
    List<DocumentPerspective> findAllByIndex(int index, int id);

}