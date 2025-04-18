package com.example.arrowdb.services;

import com.example.arrowdb.entity.SpecialCloth;
import com.example.arrowdb.repositories.SpecialClothRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SpecialClothServiceImpl implements SpecialClothService{

    private final SpecialClothRepository specialClothRepository;

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<SpecialCloth> findAllSpecialCloths() {
        return specialClothRepository.findAll().stream()
                .sorted(Comparator.comparingInt(SpecialCloth::getSpecClothId))
                .toList();
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public SpecialCloth findSpecialClothById(Integer id) {
        SpecialCloth specialCloth = null;
        Optional<SpecialCloth> optional = specialClothRepository.findById(id);
        if (optional.isPresent()){
            specialCloth = optional.get();
        }
        return specialCloth;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void saveSpecialCloth(SpecialCloth specialCloth) {
        specialClothRepository.save(specialCloth);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void deleteSpecialClothById(Integer id) {
        specialClothRepository.deleteById(id);
    }
}