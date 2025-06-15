package com.project.appvault.service;

import com.project.appvault.entity.Client;
import com.project.appvault.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @Override
    public Client getClientById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client with ID " + id + " not found"));
    }

    @Override
    public void saveClient(Client client) {
        if (clientRepository.findByName(client.getName()).isPresent()) {
            throw new RuntimeException("Client name already exists");
        }
        clientRepository.save(client);
    }

    @Override
    public void updateClient(Client client) {
        Client existing = clientRepository.findById(client.getId())
                .orElseThrow(() -> new RuntimeException("Client not found"));

        if (!existing.getName().equals(client.getName()) &&
                clientRepository.findByName(client.getName()).isPresent()) {
            throw new RuntimeException("Client name already exists");
        }

        existing.setName(client.getName());
        existing.setDescription(client.getDescription());
        clientRepository.save(existing);
    }

    @Override
    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }

    @Override
    public boolean isNameTakenByOther(String name, Long clientId) {
        return clientRepository.findByName(name)
                .filter(client -> !client.getId().equals(clientId))
                .isPresent();
    }
}
