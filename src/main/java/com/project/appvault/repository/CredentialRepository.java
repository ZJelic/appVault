package com.project.appvault.repository;

import com.project.appvault.entity.Credential;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CredentialRepository extends JpaRepository<Credential, Long> {
    List<Credential> findByProjectId(Long projectId);
    List<Credential> findByCredentialTypeId(Long credentialTypeId);
    boolean existsByNameAndProjectId(String name, Long projectId);
}
