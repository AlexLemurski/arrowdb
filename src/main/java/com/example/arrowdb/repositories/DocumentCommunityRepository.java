package com.example.arrowdb.repositories;

import com.example.arrowdb.entity.DocumentCommunity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentCommunityRepository extends JpaRepository<DocumentCommunity, Integer> {

}