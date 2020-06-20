package com.softobt.asgardian.control.service;

import com.softobt.asgardian.control.config.JWTokenUtil;
import com.softobt.core.exceptions.models.CredentialException;
import com.softobt.asgardian.control.models.AsgardianUser;
import com.softobt.asgardian.control.repositories.AsgardianUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

/**
 * @author aobeitor
 * @since 6/1/20
 */
public abstract class AsgardianUserValidationService implements UserValidationService{
    protected AsgardianUserRepository userRepository;

    protected PasswordEncoder passwordEncoder;

    protected JWTokenUtil tokenUtil;

    public final AsgardianUser validateUserCredentials(String usernmae, String password, String authDomain)throws CredentialException{
        Optional<AsgardianUser> optional = this.userRepository.findByUsernameAndDomain_Name(usernmae,authDomain);
        if(!optional.isPresent()){
            throw new CredentialException(CredentialException.CredentialExceptionType.UNKNOWN_USER);
        }
        AsgardianUser user = optional.get();
        if(this.passwordEncoder.matches(password,user.getPassword()) && user.isActive()){
            return user;
        }
        throw new CredentialException(CredentialException.CredentialExceptionType.INVALID_PASSWORD);
    }



}
