package com.example.arrowdb.services;

import com.example.arrowdb.entity.PerspectiveObject;
import com.example.arrowdb.repositories.PerspectiveObjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PerspectiveObjectServiceImpl implements PerspectiveObjectService {

    private final PerspectiveObjectRepository perspectiveObjectRepository;

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<PerspectiveObject> findAllPerspectiveObjects() {
        return perspectiveObjectRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public PerspectiveObject findPerspectiveObjectById(Integer id) {
        PerspectiveObject perspectiveObject = null;
        Optional<PerspectiveObject> optionalPerspectiveObject = perspectiveObjectRepository.findById(id);
        if(optionalPerspectiveObject.isPresent()) {
            perspectiveObject = optionalPerspectiveObject.get();
        }
        return perspectiveObject;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void savePerspectiveObject(PerspectiveObject perspectiveObject) {
        perspectiveObjectRepository.save(perspectiveObject);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void deletePerspectiveObjectById(Integer id) {
        perspectiveObjectRepository.deleteById(id);
    }

}