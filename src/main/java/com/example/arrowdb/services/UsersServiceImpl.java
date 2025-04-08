package com.example.arrowdb.services;

import com.example.arrowdb.entity.Users;
import com.example.arrowdb.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService{

    private final UsersRepository usersRepository;

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<Users> findAllUsers() {
        return usersRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public Users findUserById(Integer id) {
        Users users = null;
        Optional<Users> usersOptional = usersRepository.findById(id);
        if (usersOptional.isPresent()) {
            users = usersOptional.get();
        }
        return users;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Users createUser(Users user) {
        return usersRepository.save(user);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void updateUser(Users user) {
        usersRepository.save(user);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void deleteUser(Integer id) {
        usersRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public Users findUsersByUserName(String name) {
        Users users = null;
        Optional<Users> usersOptional = usersRepository.findUsersByUserName(name);
        if (usersOptional.isPresent()) {
            users = usersOptional.get();
        }
        return users;
    }

}