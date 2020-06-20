package com.softobt.asgardian.control.service;

import com.softobt.asgardian.control.apimodels.LoggedInUser;
import com.softobt.core.exceptions.models.CredentialException;

/**
 * @author aobeitor
 * @since 6/8/20
 */
public interface AuthorizationService {
    void preAuthorize(LoggedInUser user, String minimumAuthority)throws CredentialException;
}
