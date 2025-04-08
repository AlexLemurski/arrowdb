package com.example.arrowdb.repositories;

import com.example.arrowdb.entity.DocumentProfession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentProfessionRepository extends JpaRepository<DocumentProfession, Integer> {

}