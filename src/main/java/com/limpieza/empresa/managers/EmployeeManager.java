package com.limpieza.empresa.managers;

import com.limpieza.empresa.interfaces.IEmployeeManager;
import com.limpieza.empresa.interfaces.EmployeeRepository;
import com.limpieza.empresa.models.Employee;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * MANAGER: Gestiona la creación, validación, edición y eliminación de empleados.
 *
 * SOLID - S (Single Responsibility): Solo maneja la persistencia de empleados y su validación.
 */
@Service
@Transactional
public class EmployeeManager implements IEmployeeManager {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public boolean addEmployee(String name, String id, String role) {
        if (!isValidName(name)) {
            System.out.println("  [Error] Nombre inválido. No debe estar vacío y máximo 50 caracteres.");
            return false;
        }
        if (!isValidId(id)) {
            System.out.println("  [Error] ID inválido. Solo letras y números, máximo 10 caracteres.");
            return false;
        }
        if (isDuplicateId(id)) {
            System.out.println("  [Error] Ya existe un empleado con el ID: " + id.trim());
            return false;
        }
        if (!isValidRole(role)) {
            System.out.println("  [Error] Rol inválido. No debe estar vacío y máximo 30 caracteres.");
            return false;
        }

        Employee employee = new Employee(name.trim(), id.trim().toUpperCase(), role.trim());
        employeeRepository.save(employee);
        return true;
    }

    @Override
    public boolean removeEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            System.out.println("  [Error] El empleado con ID " + id + " no existe.");
            return false;
        }
        employeeRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean updateEmployeeName(Long id, String newName) {
        Employee employee = employeeRepository.findById(id).orElse(null);
        if (employee == null) {
            System.out.println("  [Error] El empleado con ID " + id + " no existe.");
            return false;
        }
        if (!isValidName(newName)) {
            System.out.println("  [Error] Nombre inválido.");
            return false;
        }
        employee.setName(newName.trim());
        employeeRepository.save(employee);
        return true;
    }

    @Override
    public boolean updateEmployeeId(Long id, String newId) {
        Employee employee = employeeRepository.findById(id).orElse(null);
        if (employee == null) {
            System.out.println("  [Error] El empleado con ID " + id + " no existe.");
            return false;
        }
        if (!isValidId(newId)) {
            System.out.println("  [Error] ID inválido.");
            return false;
        }
        if (isDuplicateIdExcept(newId, id)) {
            System.out.println("  [Error] Ya existe un empleado con ese ID.");
            return false;
        }
        employee.setIdnt(newId.trim().toUpperCase());
        employeeRepository.save(employee);
        return true;
    }

    @Override
    public boolean updateEmployeeRole(Long id, String newRole) {
        Employee employee = employeeRepository.findById(id).orElse(null);
        if (employee == null) {
            System.out.println("  [Error] El empleado con ID " + id + " no existe.");
            return false;
        }
        if (!isValidRole(newRole)) {
            System.out.println("  [Error] Rol inválido.");
            return false;
        }
        employee.setRole(newRole.trim());
        employeeRepository.save(employee);
        return true;
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee getEmployee(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    @Override
    public long getEmployeeCount() {
        return employeeRepository.count();
    }

    private boolean isValidName(String name) {
        return name != null
            && !name.trim().isEmpty()
            && name.trim().length() <= 50;
    }

    private boolean isValidId(String id) {
        if (id == null || id.trim().isEmpty()) return false;
        String trimmed = id.trim();
        return trimmed.matches("[a-zA-Z0-9]+") && trimmed.length() <= 10;
    }

    private boolean isValidRole(String role) {
        return role != null
            && !role.trim().isEmpty()
            && role.trim().length() <= 30;
    }

    private boolean isDuplicateId(String id) {
        String normalized = id.trim().toUpperCase();
        return employeeRepository.findByIdnt(normalized).isPresent();
    }

    private boolean isDuplicateIdExcept(String id, Long currentId) {
        String normalized = id.trim().toUpperCase();
        var existing = employeeRepository.findByIdnt(normalized);
        return existing.isPresent() && !existing.get().getId().equals(currentId);
    }
}
