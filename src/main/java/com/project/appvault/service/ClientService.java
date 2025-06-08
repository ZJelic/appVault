package com.project.appvault.service;

import com.project.appvault.entity.Client;

import java.util.List;

public interface ClientService {
    List<Client> getAllClients();
    Client getClientById(Long id);
    void saveClient(Client client);
    void updateClient(Client client);
    void deleteClient(Long id);
    boolean isNameTakenByOther(String name, Long clientId);
}
