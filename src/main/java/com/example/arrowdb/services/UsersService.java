package com.example.arrowdb.services;

import com.example.arrowdb.entity.Users;

import java.util.List;

public interface UsersService {

    List<Users> findAllUsers();
    Users findUserById(Integer id);
    Users createUser(Users user);
    void updateUser(Users user);
    void deleteUser(Integer id);

    Users findUsersByUserName(String name);

}