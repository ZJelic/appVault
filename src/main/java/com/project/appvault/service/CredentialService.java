package com.project.appvault.service;

import com.project.appvault.entity.Credential;

import java.util.List;

public interface CredentialService {
    List<Credential> getAllCredentials();
    List<Credential> getCredentialsByProject(Long projectId);
    List<Credential> getCredentialsByType(Long credentialTypeId);
    Credential getCredentialById(Long id);
    void saveCredential(Credential credential);
    void updateCredential(Credential credential);
    void deleteCredential(Long id);
    boolean isNameTakenInProject(String name, Long projectId, Long excludeId);
}
