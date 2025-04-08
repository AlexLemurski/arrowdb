package com.example.arrowdb.repositories;

import com.example.arrowdb.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Integer> {

    @Query("""
            select r from Roles r
            where r.menuName=:name
            """)
    List<Roles> findRolesByMenuName(String name);

}