package com.limpieza.empresa.controllers;

import com.limpieza.empresa.interfaces.IEmployeeManager;
import com.limpieza.empresa.models.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



import java.util.List;

@Controller
public class EmployeeController {

    @Autowired
    private IEmployeeManager employeeManager;

    @GetMapping("/employees")
    public String listEmployees(Model model) {
        List<Employee> employees = employeeManager.getAllEmployees();
        model.addAttribute("employees", employees);
        model.addAttribute("section", "employees");
        return "employees";
    }

    @PostMapping("/employees/search")
    public String searchEmployees(@RequestParam String name, Model model) {
        List<Employee> employees = employeeManager.getAllEmployees()
            .stream()
            .filter(e -> e.getName().toLowerCase().contains(name.toLowerCase()))
            .toList();
        model.addAttribute("employees", employees);
        model.addAttribute("section", "employees");
        model.addAttribute("searchTerm", name);
        return "employees";
    }

    @PostMapping("/employees/save")
    public String saveEmployee(@RequestParam String name,
                               @RequestParam String idnt,
                               @RequestParam String role,
                               RedirectAttributes redirectAttributes) {
        boolean success = employeeManager.addEmployee(name, idnt, role);
        if (success) {
            ControllerUtils.addFlashMessage(redirectAttributes, "Empleado agregado exitosamente", "success");
        } else {
            ControllerUtils.addFlashMessage(redirectAttributes, "Error al agregar empleado", "error");
        }
        return "redirect:/employees";
    }

    @GetMapping("/employees/edit/{id}")
    public String editEmployeeForm(@PathVariable Long id, Model model) {
        Employee employee = employeeManager.getEmployee(id);
        if (employee == null) {
            return "redirect:/employees";
        }
        List<Employee> employees = employeeManager.getAllEmployees();
        model.addAttribute("employees", employees);
        model.addAttribute("editingEmployee", employee);
        model.addAttribute("section", "employees");
        return "employees";
    }

    @PostMapping("/employees/update/{id}")
    public String updateEmployee(@PathVariable Long id,
                                 @RequestParam String field,
                                 @RequestParam String value,
                                 RedirectAttributes redirectAttributes) {
        boolean success = false;
        switch(field) {
            case "name":
                success = employeeManager.updateEmployeeName(id, value);
                break;
            case "idnt":
                success = employeeManager.updateEmployeeId(id, value);
                break;
            case "role":
                success = employeeManager.updateEmployeeRole(id, value);
                break;
        }
        if (success) {
            ControllerUtils.addFlashMessage(redirectAttributes, "Empleado actualizado", "success");
        } else {
            ControllerUtils.addFlashMessage(redirectAttributes, "Error al actualizar", "error");
        }
        return "redirect:/employees";
    }

    @PostMapping("/employees/delete/{id}")
    public String deleteEmployee(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean success = employeeManager.removeEmployee(id);
        if (success) {
            ControllerUtils.addFlashMessage(redirectAttributes, "Empleado eliminado", "success");
        } else {
            ControllerUtils.addFlashMessage(redirectAttributes, "Error al eliminar", "error");
        }
        return "redirect:/employees";
    }
}
