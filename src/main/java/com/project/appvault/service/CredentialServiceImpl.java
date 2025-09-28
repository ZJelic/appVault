package com.project.appvault.service;

import com.project.appvault.entity.Credential;
import com.project.appvault.repository.CredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CredentialServiceImpl implements CredentialService {

    private final CredentialRepository credentialRepository;

    @Override
    public List<Credential> getAllCredentials() {
        return credentialRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @Override
    public List<Credential> getCredentialsByProject(Long projectId) {
        return credentialRepository.findByProjectId(projectId);
    }

    @Override
    public List<Credential> getCredentialsByType(Long credentialTypeId) {
        return credentialRepository.findByCredentialTypeId(credentialTypeId);
    }

    @Override
    public Credential getCredentialById(Long id) {
        return credentialRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Credential not found with id " + id));
    }

    @Override
    public void saveCredential(Credential credential) {
        if (isNameTakenInProject(credential.getName(), credential.getProject().getId(), null)) {
            throw new IllegalArgumentException(
                    "Credential name '" + credential.getName() + "' already exists in project " + credential.getProject().getName()
            );
        }
        credentialRepository.save(credential);
    }

    @Override
    public void updateCredential(Credential credential) {
        if (isNameTakenInProject(credential.getName(), credential.getProject().getId(), credential.getId())) {
            throw new IllegalArgumentException(
                    "Credential name '" + credential.getName() + "' already exists in project " + credential.getProject().getName()
            );
        }
        credentialRepository.save(credential);
    }

    @Override
    public void deleteCredential(Long id) {
        credentialRepository.deleteById(id);
    }

    @Override
    public boolean isNameTakenInProject(String name, Long projectId, Long excludeId) {
        return credentialRepository.existsByNameAndProjectId(name, projectId)
                && (excludeId == null || !credentialRepository.findById(excludeId)
                .map(c -> c.getName().equals(name) && c.getProject().getId().equals(projectId))
                .orElse(false));
    }
}
