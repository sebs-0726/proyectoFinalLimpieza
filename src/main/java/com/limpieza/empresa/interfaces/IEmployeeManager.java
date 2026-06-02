package com.limpieza.empresa.interfaces;

import com.limpieza.empresa.models.Employee;
import java.util.List;

/**
 * INTERFAZ: Contrato para gestionar empleados.
 *
 * SOLID - D (Dependency Inversion) y SOLID - I (Interface Segregation):
 * Ver explicación en IClientManager.
 */
public interface IEmployeeManager {

    boolean addEmployee(String name, String id, String role);
    boolean removeEmployee(Long id);

    boolean updateEmployeeName(Long id, String newName);
    boolean updateEmployeeId(Long id, String newId);
    boolean updateEmployeeRole(Long id, String newRole);

    List<Employee> getAllEmployees();
    Employee getEmployee(Long id);
    long getEmployeeCount();
}