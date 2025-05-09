package com.example.arrowdb.services;

import com.example.arrowdb.entity.PersonalInstrument;
import com.example.arrowdb.repositories.PersonalInstrumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonalInstrumentServiceImpl implements PersonalInstrumentService {

    private final PersonalInstrumentRepository personalInstrumentRepository;

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<PersonalInstrument> findAllPersonalInstruments() {
        return personalInstrumentRepository.findAll().stream()
                .sorted(Comparator.comparingInt(PersonalInstrument::getPersonalInstrId))
                .toList();
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public PersonalInstrument findPersonalInstrumentById(Integer id) {
        PersonalInstrument instruments = null;
        Optional<PersonalInstrument> optional = personalInstrumentRepository.findById(id);
        if (optional.isPresent()){
            instruments = optional.get();
        }
        return instruments;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void savePersonalInstrument(PersonalInstrument instruments) {
        personalInstrumentRepository.save(instruments);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void deletePersonalInstrumentsById(Integer id) {
        personalInstrumentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public Integer findPersonalInstIdByEmployeeId(Integer id) {
        return personalInstrumentRepository.findPersonalInstIdByEmployeeId(id);
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<PersonalInstrument> findPersonalInstrumentForCatalogueMenu() {
        return personalInstrumentRepository.findPersonalInstrumentForCatalogueMenu();
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<PersonalInstrument> findAllActiveAndFreePersonalInstrument() {
        return personalInstrumentRepository.findAllActiveAndFreePersonalInstrument();
    }

}
