package com.softobt.asgardian.control.service;

import com.softobt.asgardian.control.config.JWTokenUtil;
import com.softobt.asgardian.control.repositories.BaseDomainRepository;
import com.softobt.core.exceptions.models.CredentialException;
import com.softobt.asgardian.control.models.AsgardianUser;
import com.softobt.asgardian.control.repositories.AsgardianUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author aobeitor
 * @since 6/1/20
 */
public abstract class AsgardianUserValidationService<T extends AsgardianUser> implements UserValidationService{
    protected AsgardianUserRepository userRepository;

    protected BaseDomainRepository domainRepository;

    protected PasswordEncoder passwordEncoder;

    protected JWTokenUtil tokenUtil;

    public final T validateUserCredentials(String usernmae, String password, String authDomain)throws CredentialException{
        Optional<T> optional = this.userRepository.findByUsernameAndDomain_Name(usernmae,authDomain);
        if(!optional.isPresent()){
            throw new CredentialException(CredentialException.CredentialExceptionType.UNKNOWN_USER);
        }
        T user = optional.get();

        if(this.passwordEncoder.matches(password,user.getPassword()) && user.isActive()){
            userRepository.updateLastLogin(LocalDateTime.now(),usernmae,domainRepository.findFirstByName(authDomain).get());
            return user;
        }
        throw new CredentialException(CredentialException.CredentialExceptionType.INVALID_PASSWORD);
    }



}
