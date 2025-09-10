package com.project.appvault.service;

import com.project.appvault.entity.CredentialType;
import java.util.List;

public interface CredentialTypeService {
    List<CredentialType> getAllCredentialTypes();
    CredentialType getCredentialTypeById(Long id);
    void saveCredentialType(CredentialType credentialType);
    void updateCredentialType(CredentialType credentialType);
    void deleteCredentialType(Long id);
    boolean isNameTakenByOther(String name, Long id);
}
