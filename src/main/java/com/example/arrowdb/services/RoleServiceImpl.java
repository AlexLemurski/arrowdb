package com.example.arrowdb.services;

import com.example.arrowdb.entity.Roles;
import com.example.arrowdb.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<Roles> findRolesByMenuName(String name) {
        return roleRepository.findRolesByMenuName(name);
    }

}