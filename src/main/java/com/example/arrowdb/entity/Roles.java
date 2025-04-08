package com.example.arrowdb.entity;

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
@Table(schema = "security", name = "roles")
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "role_name", unique = true)
    private String roleName;

    @Column(name = "menu_name")
    private String menuName;

    @Column(name = "local_id")
    private Integer localId;

    @Column(name = "annotation")
    private String annotation;

    @ManyToMany(mappedBy = "rolesSet", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private Set<Users> users = new HashSet<>();

    @Override
    public String toString() {
        return roleName;
    }
}
