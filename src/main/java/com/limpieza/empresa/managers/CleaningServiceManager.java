package com.limpieza.empresa.managers;

import com.limpieza.empresa.enums.ServiceStatus;
import com.limpieza.empresa.interfaces.ICleaningServiceManager;
import com.limpieza.empresa.interfaces.CleaningServiceRepository;
import com.limpieza.empresa.interfaces.EmployeeRepository;
import com.limpieza.empresa.models.CleaningService;
import com.limpieza.empresa.models.Employee;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * MANAGER: Gestiona la creación, validación, edición y eliminación de servicios.
 *
 * SOLID - S (Single Responsibility): Solo maneja la persistencia de servicios de limpieza.
 */
@Service
@Transactional
public class CleaningServiceManager implements ICleaningServiceManager {

    @Autowired
    private CleaningServiceRepository serviceRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public boolean addService(String clientName, List<Long> employeeIds,
                              LocalDateTime scheduledDate, double cost) {
        if (!isValidName(clientName)) {
            System.out.println("  [Error] Nombre de cliente inválido.");
            return false;
        }
        if (!isValidEmployeeIdList(employeeIds)) {
            System.out.println("  [Error] Debe asignar al menos un empleado al servicio.");
            return false;
        }
        if (!isValidDate(scheduledDate)) {
            System.out.println("  [Error] Fecha no válida.");
            return false;
        }
        if (!isValidCost(cost)) {
            System.out.println("  [Error] El costo debe ser un valor mayor a cero.");
            return false;
        }

        List<Employee> employees = employeeRepository.findAllById(employeeIds);
        if (employees.isEmpty() || employees.size() != employeeIds.size()) {
            System.out.println("  [Error] Algunos empleados especificados no existen.");
            return false;
        }

        CleaningService service = new CleaningService(
            clientName.trim(),
            employees,
            scheduledDate,
            cost
        );
        serviceRepository.save(service);
        return true;
    }

    @Override
    public boolean removeService(Long id) {
        if (!serviceRepository.existsById(id)) {
            System.out.println("  [Error] El servicio con ID " + id + " no existe.");
            return false;
        }
        serviceRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean updateServiceDate(Long id, LocalDateTime newDate) {
        CleaningService service = serviceRepository.findById(id).orElse(null);
        if (service == null) {
            System.out.println("  [Error] El servicio con ID " + id + " no existe.");
            return false;
        }
        if (!isValidDate(newDate)) {
            System.out.println("  [Error] Fecha no válida.");
            return false;
        }
        service.setScheduledDate(newDate);
        serviceRepository.save(service);
        return true;
    }

    @Override
    public boolean updateServiceCost(Long id, double newCost) {
        CleaningService service = serviceRepository.findById(id).orElse(null);
        if (service == null) {
            System.out.println("  [Error] El servicio con ID " + id + " no existe.");
            return false;
        }
        if (!isValidCost(newCost)) {
            System.out.println("  [Error] El costo debe ser mayor a cero.");
            return false;
        }
        service.setCost(newCost);
        serviceRepository.save(service);
        return true;
    }

    @Override
    public boolean updateServiceStatus(Long id, ServiceStatus newStatus) {
        CleaningService service = serviceRepository.findById(id).orElse(null);
        if (service == null) {
            System.out.println("  [Error] El servicio con ID " + id + " no existe.");
            return false;
        }
        if (newStatus == null) {
            System.out.println("  [Error] Estado no válido.");
            return false;
        }
        service.setStatus(newStatus);
        serviceRepository.save(service);
        return true;
    }

    @Override
    public boolean updateServiceEmployees(Long id, List<Long> newEmployeeIds) {
        CleaningService service = serviceRepository.findById(id).orElse(null);
        if (service == null) {
            System.out.println("  [Error] El servicio con ID " + id + " no existe.");
            return false;
        }
        if (!isValidEmployeeIdList(newEmployeeIds)) {
            System.out.println("  [Error] Debe haber al menos un empleado.");
            return false;
        }
        List<Employee> employees = employeeRepository.findAllById(newEmployeeIds);
        if (employees.isEmpty() || employees.size() != newEmployeeIds.size()) {
            System.out.println("  [Error] Algunos empleados especificados no existen.");
            return false;
        }
        service.setEmployees(employees);
        serviceRepository.save(service);
        return true;
    }

    @Override
    public List<CleaningService> getAllServices() {
        return serviceRepository.findAll();
    }

    @Override
    public List<CleaningService> getServicesByStatus(ServiceStatus status) {
        return serviceRepository.findByStatus(status);
    }

    @Override
    public CleaningService getService(Long id) {
        return serviceRepository.findById(id).orElse(null);
    }

    @Override
    public long getServiceCount() {
        return serviceRepository.count();
    }

    private boolean isValidName(String name) {
        return name != null
            && !name.trim().isEmpty()
            && name.trim().length() <= 50;
    }

    private boolean isValidEmployeeIdList(List<Long> ids) {
        return ids != null && !ids.isEmpty();
    }

    private boolean isValidDate(LocalDateTime date) {
        return date != null;
    }

    private boolean isValidCost(double cost) {
        return cost > 0;
    }
}
