package com.limpieza.empresa.managers;

import com.limpieza.empresa.interfaces.IClientManager;
import com.limpieza.empresa.interfaces.ClientRepository;
import com.limpieza.empresa.models.Client;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * MANAGER: Gestiona la creación, validación, edición y eliminación de clientes.
 *
 * SOLID - S (Single Responsibility): Esta clase SOLO se encarga de manejar
 * la persistencia de clientes y validar sus datos.
 */
@Service
@Transactional
public class ClientManager implements IClientManager {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public boolean addClient(String name, String contact, String address) {
        if (!isValidName(name)) {
            System.out.println("  [Error] Nombre inválido. No debe estar vacío y máximo 50 caracteres.");
            return false;
        }
        if (!isValidContact(contact)) {
            System.out.println("  [Error] Contacto inválido. Solo dígitos, entre 7 y 15 caracteres.");
            return false;
        }
        if (!isValidAddress(address)) {
            System.out.println("  [Error] Dirección inválida. No debe estar vacía y máximo 100 caracteres.");
            return false;
        }

        Client client = new Client(name.trim(), contact.trim(), address.trim());
        clientRepository.save(client);
        return true;
    }

    @Override
    public boolean removeClient(Long id) {
        if (!clientRepository.existsById(id)) {
            System.out.println("  [Error] El cliente con ID " + id + " no existe.");
            return false;
        }
        clientRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean updateClientName(Long id, String newName) {
        Client client = clientRepository.findById(id).orElse(null);
        if (client == null) {
            System.out.println("  [Error] El cliente con ID " + id + " no existe.");
            return false;
        }
        if (!isValidName(newName)) {
            System.out.println("  [Error] Nombre inválido.");
            return false;
        }
        client.setName(newName.trim());
        clientRepository.save(client);
        return true;
    }

    @Override
    public boolean updateClientContact(Long id, String newContact) {
        Client client = clientRepository.findById(id).orElse(null);
        if (client == null) {
            System.out.println("  [Error] El cliente con ID " + id + " no existe.");
            return false;
        }
        if (!isValidContact(newContact)) {
            System.out.println("  [Error] Contacto inválido.");
            return false;
        }
        client.setContact(newContact.trim());
        clientRepository.save(client);
        return true;
    }

    @Override
    public boolean updateClientAddress(Long id, String newAddress) {
        Client client = clientRepository.findById(id).orElse(null);
        if (client == null) {
            System.out.println("  [Error] El cliente con ID " + id + " no existe.");
            return false;
        }
        if (!isValidAddress(newAddress)) {
            System.out.println("  [Error] Dirección inválida.");
            return false;
        }
        client.setAddress(newAddress.trim());
        clientRepository.save(client);
        return true;
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public Client getClient(Long id) {
        return clientRepository.findById(id).orElse(null);
    }

    @Override
    public long getClientCount() {
        return clientRepository.count();
    }

    private boolean isValidName(String name) {
        return name != null
            && !name.trim().isEmpty()
            && name.trim().length() <= 50;
    }

    private boolean isValidContact(String contact) {
        if (contact == null || contact.trim().isEmpty()) return false;
        String c = contact.trim();
        return c.matches("\\d+") && c.length() >= 7 && c.length() <= 15;
    }

    private boolean isValidAddress(String address) {
        return address != null
            && !address.trim().isEmpty()
            && address.trim().length() <= 100;
    }
}

