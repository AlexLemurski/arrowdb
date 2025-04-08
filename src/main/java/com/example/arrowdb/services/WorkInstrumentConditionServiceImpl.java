package com.example.arrowdb.services;

import com.example.arrowdb.entity.WorkInstrumentCondition;
import com.example.arrowdb.repositories.WorkInstrumentConditionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkInstrumentConditionServiceImpl implements WorkInstrumentConditionService{

    private final WorkInstrumentConditionRepository workInstrumentConditionRepository;

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<WorkInstrumentCondition> findWorkInstrumentConditions() {
        return workInstrumentConditionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public WorkInstrumentCondition findWorkInstrumentConditionById(Integer id) {
        WorkInstrumentCondition workInstrumentCondition = null;
        Optional<WorkInstrumentCondition> optionalWorkInstrumentCondition =
                workInstrumentConditionRepository.findById(id);
        if (optionalWorkInstrumentCondition.isPresent()) {
            workInstrumentCondition = optionalWorkInstrumentCondition.get();
        }
        return workInstrumentCondition;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void saveWorkInstrumentCondition(WorkInstrumentCondition instruments) {
        workInstrumentConditionRepository.save(instruments);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void deleteWorkInstrumentConditionById(Integer id) {
        workInstrumentConditionRepository.deleteById(id);
    }
}
