package com.example.arrowdb.entity;

import com.example.arrowdb.enums.UserStatusENUM;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(schema = "security", name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "username")
    private String userName;

    @Column(name = "password")
    private String password;

    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(schema = "security", name = "join_users_roles",
            joinColumns = @JoinColumn(name = "join_user_id"),
            inverseJoinColumns = @JoinColumn(name = "join_role_id"))
    private Set<Roles> rolesSet = new HashSet<>();

    @Column(name = "user_status")
    private UserStatusENUM userStatusENUM;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Override
    public String toString() {
        return userName;
    }
}