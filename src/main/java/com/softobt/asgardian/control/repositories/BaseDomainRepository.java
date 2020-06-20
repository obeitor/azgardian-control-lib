package com.softobt.asgardian.control.repositories;

import com.softobt.asgardian.control.models.Domain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

/**
 * @author aobeitor
 * @since 6/3/20
 */
@NoRepositoryBean
public interface BaseDomainRepository extends JpaRepository<Domain,Long>{
    Optional<Domain> findFirstByName(String name);
}
