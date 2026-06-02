package com.limpieza.empresa.models;

import com.limpieza.empresa.enums.ServiceStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

/**
 * MODELO: Servicio de Limpieza.
 * Representa un servicio registrado con cliente, empleados, fecha, costo y estado.
 */

@Entity
@Table(name = "cleaning_service")
public class CleaningService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clientName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "cleaning_service_employees",
        joinColumns = @JoinColumn(name = "cleaning_service_id"),
        inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    private List<Employee> employees = new ArrayList<>();

    private LocalDateTime scheduledDate;
    private double cost;
    private ServiceStatus status;

    public CleaningService() {}

    public CleaningService(String clientName, List<Employee> employees,
                           LocalDateTime scheduledDate, double cost) {
        this.clientName = clientName;
        this.employees = employees;
        this.scheduledDate = scheduledDate;
        this.cost = cost;
        this.status = ServiceStatus.PENDING;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getClientName() {return clientName;}
    public void setClientName(String clientName) {this.clientName = clientName;}

    public List<Employee> getEmployees() {return employees;}
    public void setEmployees(List<Employee> employees) {this.employees = employees;}

    public LocalDateTime getScheduledDate() {return scheduledDate;}
    public void setScheduledDate(LocalDateTime scheduledDate) {this.scheduledDate = scheduledDate;}

    public double getCost() {return cost;}
    public void setCost(double cost) {this.cost = cost;}

    public ServiceStatus getStatus() {return status;}
    public void setStatus(ServiceStatus status) {this.status = status;}

    @Override
    public String toString() {
        List<String> empNames = new ArrayList<>();
        for (Employee emp : employees) {
            empNames.add(emp.getName());
        }
        return "Servicio #" + id                                      + "\n" +
               "Cliente   : " + clientName                           + "\n" +
               "Empleados : " + String.join(", ", empNames)          + "\n" +
               "Fecha     : " + scheduledDate                        + "\n" +
               "Costo     : $" + String.format("%.2f", cost)         + "\n" +
               "Estado    : " + status.getLabel();
    }
}