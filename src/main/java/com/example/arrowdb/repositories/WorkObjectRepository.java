package com.example.arrowdb.repositories;

import com.example.arrowdb.entity.WorkObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkObjectRepository extends JpaRepository<WorkObject, Integer> {

    @Query("""
            select w from WorkObject w
            left join fetch w.workObjectChief
            order by w.workObjectId
            """)
    List<WorkObject> findAllWorkObjectForMainMenu();

    @Query("""
            select w from WorkObject w
            left join fetch w.workObjectChief wc
            left join fetch wc.profession
            left join fetch w.PTOList pl
            left join fetch pl.profession
            left join fetch w.constructionControlList
            left join fetch w.storeKeeperList skl
            left join fetch skl.profession
            left join fetch w.supplierList sl
            left join fetch sl.profession
            left join fetch w.workInstrumentList
            left join fetch w.measInstrumentList
            left join fetch w.documentWorkObjectComList
            where w.workObjectId=:id""")
    WorkObject findWorkObjectByIdForView(int id);

    @Query("""
            select w from WorkObject w
            left join fetch w.constructionControlList ccl
            left join fetch ccl.responsibleFromContractor rc
            left join fetch rc.profession
            left join fetch ccl.responsibleFromSKContractor rcsk
            left join fetch rcsk.profession
            where w.workObjectId=:id
            """)
    WorkObject findWorkObjectByIdForConstructionControl(int id);

    @Query("""
            select w from WorkObject w
            left join fetch w.constructionControlList
            where w.workObjectStatusENUM != 0 and w.workObjectStatusENUM != 3
            order by w.workObjectId
            """)
    List<WorkObject> findAllConstructionControlFoMainMenu();

}