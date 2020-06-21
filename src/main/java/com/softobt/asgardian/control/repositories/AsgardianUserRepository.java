package com.softobt.asgardian.control.repositories;

import com.softobt.asgardian.control.models.AsgardianUser;
import com.softobt.asgardian.control.models.Domain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author aobeitor
 * @since 5/31/20
 */
@NoRepositoryBean
public interface AsgardianUserRepository<T extends AsgardianUser> extends JpaRepository<T,Long> {

    Optional<T> findByUsernameAndDomain_Name(String username, String domainName);

    @Query("UPDATE #{#entityName} u set u.password = :password, u.passwordLastUpdate = :now where u.username = :username and u.domain = :domain")
    void updatePassword(@Param("password")String password, @Param("now")LocalDateTime now, @Param("username")String username, @Param("domain")Domain authDomain);

    @Modifying
    @Transactional
    @Query("UPDATE #{#entityName} t set t.lastLogin = :now where t.username = :username and t.domain = :domain")
    void updateLastLogin(@Param("now") LocalDateTime now,@Param("username")String username, @Param("domain")Domain domain);
}
