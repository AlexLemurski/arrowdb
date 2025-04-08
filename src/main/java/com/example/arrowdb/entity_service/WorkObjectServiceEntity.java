package com.example.arrowdb.entity_service;

import com.example.arrowdb.auxiliary.Auxiliary;
import com.example.arrowdb.entity.WorkObject;
import com.example.arrowdb.enums.WorkObjectStatusENUM;
import org.springframework.stereotype.Component;

@Component
public class WorkObjectServiceEntity extends Auxiliary {
    
    public final void addCollectionEmployeeToEnvers(WorkObject workObject){
        workObject.setPTOListString(workObject.getPTOList().toString()
                .replace("[", "").replace("]", ""));
        workObject.setSupplierListString(workObject.getSupplierList().toString()
                .replace("[", "").replace("]", ""));
        workObject.setStoreKeeperString(workObject.getStoreKeeperList().toString()
                .replace("[", "").replace("]", ""));
    }
    
    public final boolean checkWorkObjectRelationshipsBeforeClose(WorkObject workObjectById, WorkObject workObject){
        return !workObjectById.getWorkInstrumentList().isEmpty() && workObject.getWorkObjectStatusENUM()
                .equals(WorkObjectStatusENUM.CLOSED) ||
               !workObjectById.getWorkInstrumentList().isEmpty() && workObject.getWorkObjectStatusENUM()
                       .equals(WorkObjectStatusENUM.TERMINATED) ||
               !workObjectById.getMeasInstrumentList().isEmpty() && workObject.getWorkObjectStatusENUM()
                       .equals(WorkObjectStatusENUM.CLOSED) ||
               !workObjectById.getMeasInstrumentList().isEmpty() && workObject.getWorkObjectStatusENUM()
                       .equals(WorkObjectStatusENUM.TERMINATED) ||
               workObjectById.getWorkObjectChief() != null && workObject.getWorkObjectStatusENUM()
                       .equals(WorkObjectStatusENUM.CLOSED) ||
               workObjectById.getWorkObjectChief() != null && workObject.getWorkObjectStatusENUM()
                       .equals(WorkObjectStatusENUM.TERMINATED) ||
               !workObjectById.getPTOList().isEmpty() && workObject.getWorkObjectStatusENUM()
                       .equals(WorkObjectStatusENUM.CLOSED) ||
               !workObjectById.getPTOList().isEmpty() && workObject.getWorkObjectStatusENUM()
                       .equals(WorkObjectStatusENUM.TERMINATED) ||
               !workObjectById.getStoreKeeperList().isEmpty() && workObject.getWorkObjectStatusENUM()
                       .equals(WorkObjectStatusENUM.CLOSED) ||
               !workObjectById.getStoreKeeperList().isEmpty() && workObject.getWorkObjectStatusENUM()
                       .equals(WorkObjectStatusENUM.TERMINATED) ||
               !workObjectById.getSupplierList().isEmpty() && workObject.getWorkObjectStatusENUM()
                       .equals(WorkObjectStatusENUM.CLOSED) ||
               !workObjectById.getSupplierList().isEmpty() && workObject.getWorkObjectStatusENUM()
                       .equals(WorkObjectStatusENUM.TERMINATED) ||
               (workObjectById.getConstructionControlActive() != 0) &&
               workObject.getWorkObjectStatusENUM().equals(WorkObjectStatusENUM.CLOSED) ||
               (workObjectById.getConstructionControlActive() != 0) &&
               workObject.getWorkObjectStatusENUM().equals(WorkObjectStatusENUM.TERMINATED);
    }
    
}
