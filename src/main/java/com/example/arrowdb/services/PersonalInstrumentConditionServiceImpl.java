package com.example.arrowdb.services;

import com.example.arrowdb.entity.PersonalInstrumentCondition;
import com.example.arrowdb.repositories.PersonalInstrumentConditionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonalInstrumentConditionServiceImpl implements PersonalInstrumentConditionService {

    private final PersonalInstrumentConditionRepository personalInstrumentConditionRepository;

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<PersonalInstrumentCondition> findAllPersonalInstruments() {
        return personalInstrumentConditionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public PersonalInstrumentCondition findPersonalInstrumentById(Integer id) {
        PersonalInstrumentCondition personalInstrumentCondition = null;
        Optional<PersonalInstrumentCondition> optionalPersonalInstrumentCondition =
                personalInstrumentConditionRepository.findById(id);
        if (optionalPersonalInstrumentCondition.isPresent()) {
            personalInstrumentCondition = optionalPersonalInstrumentCondition.get();
        }
        return personalInstrumentCondition;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void savePersonalInstrument(PersonalInstrumentCondition instruments) {
        personalInstrumentConditionRepository.save(instruments);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void deletePersonalInstrumentsById(Integer id) {
        personalInstrumentConditionRepository.deleteById(id);
    }

}