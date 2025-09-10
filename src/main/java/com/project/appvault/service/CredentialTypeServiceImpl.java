package com.project.appvault.service;

import com.project.appvault.entity.CredentialType;
import com.project.appvault.repository.CredentialTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CredentialTypeServiceImpl implements CredentialTypeService {

    private final CredentialTypeRepository credentialTypeRepository;

    @Override
    public List<CredentialType> getAllCredentialTypes() {
        return credentialTypeRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @Override
    public CredentialType getCredentialTypeById(Long id) {
        return credentialTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Credential type not found with id " + id));
    }

    @Override
    public void saveCredentialType(CredentialType credentialType) {
        if (isNameTakenByOther(credentialType.getName(), null)) {
            throw new IllegalArgumentException("Credential type name '" + credentialType.getName() + "' already exists");
        }
        credentialTypeRepository.save(credentialType);
    }

    @Override
    public void updateCredentialType(CredentialType credentialType) {
        if (isNameTakenByOther(credentialType.getName(), credentialType.getId())) {
            throw new IllegalArgumentException("Credential type name '" + credentialType.getName() + "' already exists");
        }
        credentialTypeRepository.save(credentialType);
    }

    @Override
    public void deleteCredentialType(Long id) {
        credentialTypeRepository.deleteById(id);
    }

    @Override
    public boolean isNameTakenByOther(String name, Long id) {
        Long excludeId = id == null ? -1 : id;
        return credentialTypeRepository.findByName(name)
                .filter(ct -> !ct.getId().equals(excludeId))
                .isPresent();
    }
}
