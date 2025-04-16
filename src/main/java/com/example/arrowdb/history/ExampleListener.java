package com.example.arrowdb.history;

import com.example.arrowdb.auxiliary.UserEmployee;
import com.example.arrowdb.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ExampleListener implements RevisionListener {

    private final UserEmployee userEmployee;

//    String getUser(){
//        UserDetails userDetails = (UserDetails) SecurityContextHolder
//                .getContext()
//                .getAuthentication()
//                .getPrincipal();
//        return userDetails.getUsername();
//    }

    @Override
    public void newRevision(Object revisionEntity) {
        ExampleRevEntity exampleRevEntity = (ExampleRevEntity) revisionEntity;
//        exampleRevEntity.setUserName(getUser());
        exampleRevEntity.setUserName(userEmployee.getUserForRevision());
        exampleRevEntity.setLocalDateTimeModified(LocalDateTime.now());
    }
}