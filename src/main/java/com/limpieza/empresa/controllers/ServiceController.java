package com.limpieza.empresa.controllers;

import com.limpieza.empresa.enums.ServiceStatus;
import com.limpieza.empresa.interfaces.IEmployeeManager;
import com.limpieza.empresa.interfaces.ICleaningServiceManager;
import com.limpieza.empresa.models.CleaningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.util.List;

@Controller
public class ServiceController {

    @Autowired
    private ICleaningServiceManager serviceManager;

    @Autowired
    private IEmployeeManager employeeManager;



    @GetMapping("/services")
    public String listServices(@RequestParam(required = false) String status, Model model) {
        List<CleaningService> services;
        if (status != null && !status.isEmpty()) {
            ServiceStatus serviceStatus = ServiceStatus.valueOf(status);
            services = serviceManager.getServicesByStatus(serviceStatus);
            model.addAttribute("filterStatus", status);
        } else {
            services = serviceManager.getAllServices();
        }
        model.addAttribute("services", services);
        model.addAttribute("employees", employeeManager.getAllEmployees());
        model.addAttribute("section", "services");
        return "services";
    }

    @GetMapping("/services/edit/{id}")
    public String editServiceForm(@PathVariable Long id, Model model) {
        CleaningService service = serviceManager.getService(id);
        if (service == null) {
            return "redirect:/services";
        }
        List<CleaningService> services = serviceManager.getAllServices();
        model.addAttribute("services", services);
        model.addAttribute("editingService", service);
        model.addAttribute("employees", employeeManager.getAllEmployees());
        model.addAttribute("section", "services");
        return "services";
    }

    @PostMapping("/services/search")
    public String searchServices(@RequestParam String clientName, Model model) {
        List<CleaningService> services = serviceManager.getAllServices()
            .stream()
            .filter(s -> s.getClientName().toLowerCase().contains(clientName.toLowerCase()))
            .toList();
        model.addAttribute("services", services);
        model.addAttribute("employees", employeeManager.getAllEmployees());
        model.addAttribute("section", "services");
        model.addAttribute("searchTerm", clientName);
        return "services";
    }

    @PostMapping("/services/save")
    public String saveService(@RequestParam String clientName,
                              @RequestParam List<Long> employeeIds,
                              @RequestParam String scheduledDate,
                              @RequestParam Double cost,
                              RedirectAttributes redirectAttributes) {
        try {
            LocalDateTime dateTime = parseDate(scheduledDate);
            boolean success = serviceManager.addService(clientName, employeeIds, dateTime, cost);
            if (success) {
                ControllerUtils.addFlashMessage(redirectAttributes, "Servicio agregado exitosamente", "success");
            } else {
                ControllerUtils.addFlashMessage(redirectAttributes, "Error al agregar servicio", "error");
            }
        } catch (Exception e) {
            ControllerUtils.addFlashMessage(redirectAttributes, "Error en formato de fecha", "error");
        }
        return "redirect:/services";
    }

    @PostMapping("/services/update/{id}")
    public String updateService(@PathVariable Long id,
                                @RequestParam String field,
                                @RequestParam String value,
                                RedirectAttributes redirectAttributes) {
        boolean success = false;
        try {
            switch(field) {
                case "status":
                    ServiceStatus status = ServiceStatus.valueOf(value);
                    success = serviceManager.updateServiceStatus(id, status);
                    break;
                case "cost":
                    success = serviceManager.updateServiceCost(id, Double.parseDouble(value));
                    break;
                case "date":
                    LocalDateTime dateTime = parseDate(value);
                    success = serviceManager.updateServiceDate(id, dateTime);
                    break;
            }
        } catch (Exception e) {
            ControllerUtils.addFlashMessage(redirectAttributes, "Error al actualizar", "error");
            return "redirect:/services";
        }

        if (success) {
            ControllerUtils.addFlashMessage(redirectAttributes, "Servicio actualizado", "success");
        } else {
            ControllerUtils.addFlashMessage(redirectAttributes, "Error al actualizar", "error");
        }
        return "redirect:/services";
    }

    private LocalDateTime parseDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            throw new IllegalArgumentException("Fecha inválida");
        }

        String trimmed = dateString.trim();
        // Se espera una fecha en formato dd/MM/yyyy. La hora no es requerida;
        // se asigna el inicio del día (00:00) para mantener el tipo LocalDateTime.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = LocalDate.parse(trimmed, formatter);
        return date.atStartOfDay();
    }

    @PostMapping("/services/delete/{id}")
    public String deleteService(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean success = serviceManager.removeService(id);
        if (success) {
            ControllerUtils.addFlashMessage(redirectAttributes, "Servicio eliminado", "success");
        } else {
            ControllerUtils.addFlashMessage(redirectAttributes, "Error al eliminar", "error");
        }
        return "redirect:/services";
    }
}
