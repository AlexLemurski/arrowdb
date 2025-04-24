package com.example.arrowdb.repositories;

import com.example.arrowdb.entity.DocumentWorkObjectCom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentWorkObjectRepositoryCom
        extends JpaRepository<DocumentWorkObjectCom, Integer> {

}