package com.example.arrowdb.services;

import com.example.arrowdb.entity.WorkObject;

import java.util.List;

public interface WorkObjectService {
    List<WorkObject> findAllWorkObjects();
    WorkObject findWorkObjectById(Integer id);
    void saveWorkObject(WorkObject workObject);
    void deleteWorkObjectById(Integer id);

    List<WorkObject> findAllWorkObjectForMainMenu();
    WorkObject findWorkObjectByIdForView(int id);

    WorkObject findWorkObjectByIdForConstructionControl(int id);
    List<WorkObject> findAllConstructionControlFoMainMenu();

    List<WorkObject> findAllActiveWorkObject();

}
