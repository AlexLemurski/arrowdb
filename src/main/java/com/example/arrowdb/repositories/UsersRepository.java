package com.example.arrowdb.repositories;

import com.example.arrowdb.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {

    Optional<Users> findUsersByUserName(String name);

    @Override
    @Query("""
            select u from Users u
            left join fetch u.employee emp
            left join fetch emp.profession
            except where u.userName != 'admin'
            order by u.userId
            """)
    List<Users> findAll();

    @Override
    @Query("""
            select u from Users u
            left join fetch u.employee emp
            left join fetch emp.profession
            left join fetch u.rolesSet
            where u.userId=:id
            """)
    Optional<Users> findById(Integer id);

}