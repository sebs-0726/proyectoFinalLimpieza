package com.limpieza.empresa.interfaces;

import com.limpieza.empresa.models.Employee;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByIdnt(String idnt);
    List<Employee> findByNameContainingIgnoreCase(String name);
}
