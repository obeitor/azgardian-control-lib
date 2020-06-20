package com.softobt.asgardian.control.repositories;

import com.softobt.asgardian.control.models.AsgardianUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author aobeitor
 * @since 5/31/20
 */
@NoRepositoryBean
public interface AsgardianUserRepository<T extends AsgardianUser,ID> extends JpaRepository<AsgardianUser,ID> {

    Optional<T> findByUsernameAndDomain_Name(String username, String domainName);

    //@Query("UPDATE AsgardianUser u set u.password = :password, u.passwordLastUpdate = :now where u.username = :username and u.domain = :domain")
    //void updatePassword(@Param("password")String password, @Param("now")LocalDateTime now, @Param("username")String username, @Param("domain")String authDomain);
}
