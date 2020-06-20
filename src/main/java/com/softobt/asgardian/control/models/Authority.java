package com.softobt.asgardian.control.models;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

/**
 * @author aobeitor
 * @since 5/31/20
 */
@Entity
@Table(name = "authorities")
public class Authority implements GrantedAuthority{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true, length = 45)
    private String name;

    private String description;

    private int level;

    private boolean bounded;

    public Authority() {
    }

    public Authority(String name, String description, int level) {
        this.name = name;
        this.description = description;
        this.level = level;
        this.bounded = true;
    }

    public Authority(String name, String description, int level, boolean bounded) {
        this.name = name;
        this.description = description;
        this.level = level;
        this.bounded = bounded;
    }

    @Override
    public String getAuthority() {
        return this.name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isBounded() {
        return bounded;
    }

    public void setBounded(boolean bounded) {
        this.bounded = bounded;
    }
}
