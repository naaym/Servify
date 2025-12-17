package com.servify.search.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "governorates")
public class GovernorateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String code;

    @PrePersist
    @PreUpdate
    void normalize() {
        if (name != null) {
            name = name.trim();
        }
        if (code != null) {
            code = code.trim().toLowerCase();
        }
    }
}
