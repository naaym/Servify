package com.servify.search.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "services")
public class ServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(nullable = false)
    private boolean active = true;

    @PrePersist
    @PreUpdate
    void normalize() {
        if (name != null) {
            name = name.trim();
        }
        if (slug == null && name != null) {
            slug = slugify(name);
        }
        if (slug != null) {
            slug = slugify(slug);
        }
    }

    private String slugify(String value) {
        return value
                .trim()
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-+|-+$)", "");
    }
}
