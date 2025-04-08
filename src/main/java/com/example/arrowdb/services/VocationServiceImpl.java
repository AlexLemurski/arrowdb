package com.example.arrowdb.services;

import com.example.arrowdb.entity.Vocation;
import com.example.arrowdb.repositories.VocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VocationServiceImpl implements VocationService {

    private final VocationRepository vocationRepository;

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void saveVocation(Vocation vocation) {
        vocationRepository.save(vocation);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void deleteVocationById(Integer id) {
        vocationRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<Vocation> findByVocationsByEmployeeId(int id) {
        return vocationRepository.findByVocationsByEmployeeId(id).stream()
                .sorted(Comparator.comparing(Vocation::getStartOfVocation))
                .toList();
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public Integer findEmployeeByVocationId(Integer id) {
        return vocationRepository.findEmployeeByVocationId(id);
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<Vocation> daysOfVocations(int id) {
        return vocationRepository.daysOfVocations(id);
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<Vocation> findAllVocations() {
        return vocationRepository.findAll();
    }

}