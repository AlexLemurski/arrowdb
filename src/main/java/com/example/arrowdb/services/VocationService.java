package com.example.arrowdb.services;

import com.example.arrowdb.entity.Vocation;

import java.util.List;

public interface VocationService  {

    void saveVocation(Vocation vocation);
    void deleteVocationById(Integer id);
    List<Vocation> findByVocationsByEmployeeId(int id);
    Integer findEmployeeByVocationId(Integer id);
    List<Vocation> daysOfVocations(int id);
    List<Vocation> findAllVocations();

}