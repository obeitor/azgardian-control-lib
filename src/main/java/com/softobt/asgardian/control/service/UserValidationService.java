package com.softobt.asgardian.control.service;

import com.softobt.core.api.TokenDetail;
import com.softobt.asgardian.control.config.JWTokenUtil;
import com.softobt.core.exceptions.models.CredentialException;
import com.softobt.asgardian.control.models.AsgardianUser;
import com.softobt.asgardian.control.repositories.AsgardianUserRepository;
import com.softobt.core.exceptions.models.RestServiceException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author aobeitor
 * @since 5/31/20
 */

public interface UserValidationService {

    AsgardianUser validateUserCredentials(String username, String authenticationDomain, String password)throws CredentialException;

    TokenDetail getToken(AsgardianUser user);

    AsgardianUser changePassword(String username, String authenticationDomain, String newPassword)throws RestServiceException;

    AsgardianUser saveNewUser(AsgardianUser user)throws RestServiceException;

    void setTokenUtil(JWTokenUtil tokenUtil);

    void setUserRepository(AsgardianUserRepository userRepository);

    void setPasswordEncoder(PasswordEncoder passwordEncoder);
}
