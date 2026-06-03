package com.limpieza.empresa.interfaces;

import com.limpieza.empresa.models.Client;
import java.util.List;


public interface IClientManager {

    /**
     * Valida los datos y crea un nuevo cliente.
     * @return true si se creó correctamente, false si hubo un error de validación.
     */
    boolean addClient(String name, String contact, String address);

    /**
     * Elimina el cliente por ID.
     * @return true si se eliminó, false si el ID no existe.
     */
    boolean removeClient(Long id);

    boolean updateClientName(Long id, String newName);
    boolean updateClientContact(Long id, String newContact);
    boolean updateClientAddress(Long id, String newAddress);

    List<Client> getAllClients();

    Client getClient(Long id);

    long getClientCount();
}
