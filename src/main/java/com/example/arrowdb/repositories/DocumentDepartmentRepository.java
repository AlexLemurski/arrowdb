package com.example.arrowdb.repositories;

import com.example.arrowdb.entity.DocumentDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentDepartmentRepository extends JpaRepository<DocumentDepartment, Integer> {

}