package com.example.arrowdb.services;

import com.example.arrowdb.entity.PersonalInstrument;

import java.util.List;

public interface PersonalInstrumentService {

    List<PersonalInstrument> findAllPersonalInstruments();
    PersonalInstrument findPersonalInstrumentById(Integer id);
    void savePersonalInstrument(PersonalInstrument instruments);
    void deletePersonalInstrumentsById(Integer id);
    Integer findPersonalInstIdByEmployeeId(Integer id);

    List<PersonalInstrument> findPersonalInstrumentForCatalogueMenu();
    List<PersonalInstrument> findAllActiveAndFreePersonalInstrument();

}
