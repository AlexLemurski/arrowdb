package com.example.arrowdb.services;

import com.example.arrowdb.entity.WorkInstrument;
import com.example.arrowdb.repositories.WorkInstrumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkInstrumentServiceImpl implements WorkInstrumentService{

    private final WorkInstrumentRepository workInstrumentRepository;

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<WorkInstrument> findAllWorkInstruments() {
        return workInstrumentRepository.findAll().stream()
                .sorted(Comparator.comparingInt(WorkInstrument::getWorkInstrId))
                .toList();
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public WorkInstrument findWorkInstrumentById(Integer id) {
        WorkInstrument workInstrument = null;
        Optional<WorkInstrument> optional = workInstrumentRepository.findById(id);
        if(optional.isPresent()){
            workInstrument = optional.get();
        }
        return workInstrument;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void saveWorkInstrument(WorkInstrument instruments) {
        workInstrumentRepository.save(instruments);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void deleteWorkInstrumentsById(Integer id) {
        workInstrumentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public Integer findEmployeeIdByWorkInstId(Integer id) {
        return workInstrumentRepository.findEmployeeIdByWorkInstId(id);
    }

}
