package com.limpieza.empresa.interfaces;

import com.limpieza.empresa.models.CleaningService;
import com.limpieza.empresa.enums.ServiceStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CleaningServiceRepository extends JpaRepository<CleaningService, Long> {
    List<CleaningService> findByStatus(ServiceStatus status);
    List<CleaningService> findByClientNameContainingIgnoreCase(String clientName);
}