package com.limpieza.empresa.interfaces;

import com.limpieza.empresa.enums.ServiceStatus;
import com.limpieza.empresa.models.CleaningService;
import java.time.LocalDateTime;
import java.util.List;


public interface ICleaningServiceManager {

    boolean addService(String clientName, List<Long> employeeIds,
                       LocalDateTime scheduledDate, double cost);

    boolean removeService(Long id);

    boolean updateServiceDate(Long id, LocalDateTime newDate);
    boolean updateServiceCost(Long id, double newCost);
    boolean updateServiceStatus(Long id, ServiceStatus newStatus);
    boolean updateServiceEmployees(Long id, List<Long> newEmployeeIds);

    List<CleaningService> getAllServices();

    List<CleaningService> getServicesByStatus(ServiceStatus status);

    CleaningService getService(Long id);
    long getServiceCount();
}