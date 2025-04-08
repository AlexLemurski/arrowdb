package com.example.arrowdb.auxiliary;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class Auxiliary {

    public final UserDetails getUser() {
        return (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    protected final boolean checkCreateOrAdminRole(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                       .toList()
                       .contains(new SimpleGrantedAuthority("ROLE_EMPLOYEE_CREATE"))
               ||
               userDetails.getAuthorities().stream()
                       .toList()
                       .contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

}
