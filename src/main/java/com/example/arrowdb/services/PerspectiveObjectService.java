package com.example.arrowdb.services;

import com.example.arrowdb.entity.PerspectiveObject;

import java.util.List;

public interface PerspectiveObjectService {

    List<PerspectiveObject> findAllPerspectiveObjects();
    PerspectiveObject findPerspectiveObjectById(Integer id);
    void savePerspectiveObject(PerspectiveObject perspectiveObject);
    void deletePerspectiveObjectById(Integer id);

}
