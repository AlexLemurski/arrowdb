package com.example.arrowdb.repositories;

import com.example.arrowdb.entity.DocumentCommunityDeleted;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentCommunityDeleteRepository extends JpaRepository<DocumentCommunityDeleted, Integer> {

    @Query("""
            select dcd from DocumentCommunityDeleted dcd
            order by dcd.dateAndTimeLastChange desc
            """)
    List<DocumentCommunityDeleted> findAllDeleteDocuments();

}