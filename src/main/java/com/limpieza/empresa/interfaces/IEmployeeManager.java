package com.limpieza.empresa.interfaces;

import com.limpieza.empresa.models.Employee;
import java.util.List;


 
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