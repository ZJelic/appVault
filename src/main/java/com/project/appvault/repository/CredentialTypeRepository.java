package com.project.appvault.repository;

import com.project.appvault.entity.CredentialType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CredentialTypeRepository extends JpaRepository<CredentialType, Long> {
    Optional<CredentialType> findByName(String name);
}
