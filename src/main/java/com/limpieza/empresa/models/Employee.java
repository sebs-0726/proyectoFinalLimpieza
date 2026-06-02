package com.limpieza.empresa.models;

import jakarta.persistence.*;

/**
 * MODELO: Empleado.
 * Hereda el atributo 'name' de Person.
 * Agrega 'id' (identificador único) y 'role' (cargo/rol).
 *
 * Esta clase solo representa los datos del empleado.
 * La validación y gestión la hace EmployeeManager.
 */

@Entity
@Table(name = "employee")
public class Employee extends Person {

    @Column(name = "employee_id", unique = true, nullable = false, length = 10)
    private String idnt;

    private String role;

    public Employee() {}

    public Employee(String name, String idnt, String role) {
        super(name);
        this.idnt = idnt;
        this.role = role;
    }

    public String getIdnt() {return idnt;}
    public void setIdnt(String idnt) {this.idnt = idnt;}

    public String getRole() {return role;}
    public void setRole(String role) {this.role = role;}

    @Override
    public String toString() {
        return "Nombre: " + name + "\n" +
               "ID    : " + id   + "\n" +
               "Rol   : " + role;
    }
}
