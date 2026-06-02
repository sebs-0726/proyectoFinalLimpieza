package com.limpieza.empresa.controllers;

import com.limpieza.empresa.interfaces.IClientManager;
import com.limpieza.empresa.models.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.List;

@Controller
public class ClientController {

    @Autowired
    private IClientManager clientManager;

    @GetMapping("/clients")
    public String listClients(Model model) {
        List<Client> clients = clientManager.getAllClients();
        model.addAttribute("clients", clients);
        model.addAttribute("section", "clients");
        return "clients";
    }

    @PostMapping("/clients/search")
    public String searchClients(@RequestParam String name, Model model) {
        List<Client> clients = clientManager.getAllClients()
            .stream()
            .filter(c -> c.getName().toLowerCase().contains(name.toLowerCase()))
            .toList();
        model.addAttribute("clients", clients);
        model.addAttribute("section", "clients");
        model.addAttribute("searchTerm", name);
        return "clients";
    }

    @PostMapping("/clients/save")
    public String saveClient(@RequestParam String name,
                             @RequestParam String contact,
                             @RequestParam String address,
                             RedirectAttributes redirectAttributes) {
        boolean success = clientManager.addClient(name, contact, address);
        if (success) {
            ControllerUtils.addFlashMessage(redirectAttributes, "Cliente agregado exitosamente", "success");
        } else {
            ControllerUtils.addFlashMessage(redirectAttributes, "Error al agregar cliente", "error");
        }
        return "redirect:/clients";
    }

    @GetMapping("/clients/edit/{id}")
    public String editClientForm(@PathVariable Long id, Model model) {
        Client client = clientManager.getClient(id);
        if (client == null) {
            return "redirect:/clients";
        }
        List<Client> clients = clientManager.getAllClients();
        model.addAttribute("clients", clients);
        model.addAttribute("editingClient", client);
        model.addAttribute("section", "clients");
        return "clients";
    }

    @PostMapping("/clients/update/{id}")
    public String updateClient(@PathVariable Long id,
                               @RequestParam String field,
                               @RequestParam String value,
                               RedirectAttributes redirectAttributes) {
        boolean success = false;
        switch(field) {
            case "name":
                success = clientManager.updateClientName(id, value);
                break;
            case "contact":
                success = clientManager.updateClientContact(id, value);
                break;
            case "address":
                success = clientManager.updateClientAddress(id, value);
                break;
        }
        if (success) {
            ControllerUtils.addFlashMessage(redirectAttributes, "Cliente actualizado", "success");
        } else {
            ControllerUtils.addFlashMessage(redirectAttributes, "Error al actualizar", "error");
        }
        return "redirect:/clients";
    }

    @PostMapping("/clients/delete/{id}")
    public String deleteClient(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean success = clientManager.removeClient(id);
        if (success) {
            ControllerUtils.addFlashMessage(redirectAttributes, "Cliente eliminado", "success");
        } else {
            ControllerUtils.addFlashMessage(redirectAttributes, "Error al eliminar", "error");
        }
        return "redirect:/clients";
    }
}
