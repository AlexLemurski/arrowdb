package com.example.arrowdb.services;

import com.example.arrowdb.entity.MeasInstrumentCondition;
import com.example.arrowdb.repositories.MeasInstrumentConditionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MeasInstrumentConditionServiceImpl implements MeasInstrumentConditionService {

    private final MeasInstrumentConditionRepository measInstrumentConditionRepository;

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<MeasInstrumentCondition> findMeasInstrumentConditions() {
        return measInstrumentConditionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)

    public MeasInstrumentCondition findMeasInstrumentConditionById(Integer id) {
        MeasInstrumentCondition measInstrumentCondition = null;
        Optional<MeasInstrumentCondition> optionalMeasInstrumentCondition =
                measInstrumentConditionRepository.findById(id);
        if (optionalMeasInstrumentCondition.isPresent()) {
            measInstrumentCondition = optionalMeasInstrumentCondition.get();
        }
        return measInstrumentCondition;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void saveMeasInstrumentCondition(MeasInstrumentCondition instruments) {
        measInstrumentConditionRepository.save(instruments);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void deleteMeasInstrumentConditionById(Integer id) {
        measInstrumentConditionRepository.deleteById(id);
    }
}
