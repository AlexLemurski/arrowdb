package com.example.arrowdb.history;

import com.example.arrowdb.services.UsersService;
import org.hibernate.envers.RevisionListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ExampleListener implements RevisionListener {

    private final UsersService usersService;

    public ExampleListener(@Lazy UsersService usersService) {
        this.usersService = usersService;
    }

    String getUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return userDetails.getUsername();
    }

    @Override
    public void newRevision(Object revisionEntity) {
        ExampleRevEntity exampleRevEntity = (ExampleRevEntity) revisionEntity;
        try {
            exampleRevEntity.setEmployee(usersService.findUsersByUserName(getUser()).getEmployee());
        } catch (NullPointerException e) {
            e.getStackTrace();
        }
        exampleRevEntity.setLocalDateTimeModified(LocalDateTime.now());
    }
}