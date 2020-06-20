package com.softobt.asgardian.control.models;

import com.softobt.jpa.helpers.converters.DateTimeConverter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author aobeitor
 * @since 5/31/20
 */
@Entity
@Table(name = "domains")
public class Domain {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String name;

    @Convert(converter = DateTimeConverter.class)
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdOn;

    public Domain() {
    }

    public Domain(String name) {
        this.name = name;
        this.createdOn = LocalDateTime.now();
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

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }
}
