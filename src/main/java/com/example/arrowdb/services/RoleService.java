package com.example.arrowdb.services;

import com.example.arrowdb.entity.Roles;

import java.util.List;

public interface RoleService {

    List<Roles> findRolesByMenuName(String name);

}
