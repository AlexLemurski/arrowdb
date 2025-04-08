package com.example.arrowdb.services;

import com.example.arrowdb.entity.PersonalInstrumentCondition;

import java.util.List;

public interface PersonalInstrumentConditionService {

    List<PersonalInstrumentCondition> findAllPersonalInstruments();
    PersonalInstrumentCondition findPersonalInstrumentById(Integer id);
    void savePersonalInstrument(PersonalInstrumentCondition instruments);
    void deletePersonalInstrumentsById(Integer id);

}