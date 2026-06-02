package com.limpieza.empresa.ui;

import com.limpieza.empresa.enums.ServiceStatus;
import com.limpieza.empresa.interfaces.IClientManager;
import com.limpieza.empresa.interfaces.ICleaningServiceManager;
import com.limpieza.empresa.interfaces.IEmployeeManager;
import com.limpieza.empresa.models.Client;
import com.limpieza.empresa.models.CleaningService;
import com.limpieza.empresa.models.Employee;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Profile;

/**
 * CONTROLADOR PRINCIPAL: SystemController.
 *
 * Se encarga de toda la interacción con el usuario por consola:
 * mostrar menús, leer entradas, buscar registros y solicitar acciones.
 *
 * SOLID - S (Single Responsibility): Solo maneja la interfaz de usuario.
 * SOLID - D (Dependency Inversion): Depende de las interfaces.
 */
@Profile("cli")
@Component
public class SystemController {

    @Autowired
    private IClientManager clientManager;

    @Autowired
    private IEmployeeManager employeeManager;

    @Autowired
    private ICleaningServiceManager serviceManager;

    private Scanner scanner = new Scanner(System.in);
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public void start() {
        boolean running = true;
        while (running) {
            printDivider("=");
            System.out.println("       SISTEMA DE SERVICIO DE LIMPIEZA");
            printDivider("=");
            System.out.println("  1. Gestionar Clientes");
            System.out.println("  2. Gestionar Empleados");
            System.out.println("  3. Gestionar Servicios de Limpieza");
            System.out.println("  0. Salir");
            printDivider("-");
            System.out.print("  Opción: ");

            switch (readInt()) {
                case 1:  clientMenu();  break;
                case 2:  employeeMenu(); break;
                case 3:  serviceMenu(); break;
                case 0:
                    System.out.println("\n  ¡Hasta luego!\n");
                    running = false;
                    break;
                default:
                    System.out.println("  [Error] Opción no válida. Intenta de nuevo.");
            }
        }
        scanner.close();
    }

    // =========================================================
    //  MENÚ DE CLIENTES
    // =========================================================

    private void clientMenu() {
        boolean back = false;
        while (!back) {
            printDivider("-");
            System.out.println("  MENÚ - CLIENTES");
            printDivider("-");
            System.out.println("  1. Listar todos los clientes");
            System.out.println("  2. Buscar cliente por nombre");
            System.out.println("  3. Agregar nuevo cliente");
            System.out.println("  0. Volver al menú principal");
            System.out.print("  Opción: ");

            switch (readInt()) {
                case 1: listAllClients(); break;
                case 2: searchClient();   break;
                case 3: addClient();      break;
                case 0: back = true;      break;
                default: System.out.println("  [Error] Opción no válida.");
            }
        }
    }

    private void listAllClients() {
        List<Client> all = clientManager.getAllClients();
        if (all.isEmpty()) {
            System.out.println("\n  No hay clientes registrados.\n");
            return;
        }
        System.out.println("\n  --- Lista de Clientes (" + all.size() + ") ---");
        for (int i = 0; i < all.size(); i++) {
            System.out.println("\n  [" + (i + 1) + "] ID: " + all.get(i).getId());
            System.out.println("  " + all.get(i).toString().replace("\n", "\n  "));
        }
        System.out.println();
    }

    private void searchClient() {
        if (clientManager.getClientCount() == 0) {
            System.out.println("\n  No hay clientes registrados.\n");
            return;
        }
        System.out.print("\n  Nombre a buscar: ");
        String query = scanner.nextLine().trim().toLowerCase();

        List<Client> all = clientManager.getAllClients();
        List<Client> hits = new java.util.ArrayList<>();

        for (Client c : all) {
            if (c.getName().toLowerCase().contains(query)) {
                hits.add(c);
            }
        }

        if (hits.isEmpty()) {
            System.out.println("  No se encontraron clientes con ese nombre.\n");
            return;
        }

        System.out.println("\n  Resultados encontrados:");
        for (int i = 0; i < hits.size(); i++) {
            System.out.println("  [" + (i + 1) + "] " + hits.get(i).getName() + " (ID: " + hits.get(i).getId() + ")");
        }
        System.out.print("  Selecciona un resultado (0 para cancelar): ");

        int pick = readInt();
        if (pick < 1 || pick > hits.size()) return;

        clientActions(hits.get(pick - 1).getId());
    }

    private void clientActions(Long clientId) {
        boolean back = false;
        while (!back) {
            Client c = clientManager.getClient(clientId);
            if (c == null) {
                System.out.println("  [Error] El cliente fue eliminado.\n");
                back = true;
                continue;
            }

            System.out.println("\n  --- Datos del Cliente (ID: " + clientId + ") ---");
            System.out.println("  " + c.toString().replace("\n", "\n  "));
            System.out.println("\n  ¿Qué deseas hacer?");
            System.out.println("  1. Editar nombre");
            System.out.println("  2. Editar contacto");
            System.out.println("  3. Editar dirección");
            System.out.println("  4. Eliminar cliente");
            System.out.println("  0. Volver");
            System.out.print("  Opción: ");

            switch (readInt()) {
                case 1:
                    System.out.print("  Nuevo nombre: ");
                    if (clientManager.updateClientName(clientId, scanner.nextLine()))
                        System.out.println("  ✓ Nombre actualizado.");
                    break;
                case 2:
                    System.out.print("  Nuevo contacto (solo dígitos, 7-15 caracteres): ");
                    if (clientManager.updateClientContact(clientId, scanner.nextLine()))
                        System.out.println("  ✓ Contacto actualizado.");
                    break;
                case 3:
                    System.out.print("  Nueva dirección: ");
                    if (clientManager.updateClientAddress(clientId, scanner.nextLine()))
                        System.out.println("  ✓ Dirección actualizada.");
                    break;
                case 4:
                    System.out.print("  ¿Confirmas eliminar este cliente? (s/n): ");
                    if (scanner.nextLine().trim().equalsIgnoreCase("s")) {
                        if (clientManager.removeClient(clientId)) {
                            System.out.println("  ✓ Cliente eliminado.");
                            back = true;
                        }
                    }
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("  [Error] Opción no válida.");
            }
        }
    }

    private void addClient() {
        System.out.println("\n  --- Agregar Nuevo Cliente ---");
        System.out.print("  Nombre: ");
        String name = scanner.nextLine();
        System.out.print("  Contacto (solo dígitos, 7-15 caracteres): ");
        String contact = scanner.nextLine();
        System.out.print("  Dirección: ");
        String address = scanner.nextLine();

        if (clientManager.addClient(name, contact, address)) {
            System.out.println("  ✓ ¡Cliente agregado correctamente!\n");
        }
    }

    // =========================================================
    //  MENÚ DE EMPLEADOS
    // =========================================================

    private void employeeMenu() {
        boolean back = false;
        while (!back) {
            printDivider("-");
            System.out.println("  MENÚ - EMPLEADOS");
            printDivider("-");
            System.out.println("  1. Listar todos los empleados");
            System.out.println("  2. Buscar empleado por nombre");
            System.out.println("  3. Agregar nuevo empleado");
            System.out.println("  0. Volver al menú principal");
            System.out.print("  Opción: ");

            switch (readInt()) {
                case 1: listAllEmployees(); break;
                case 2: searchEmployee();   break;
                case 3: addEmployee();      break;
                case 0: back = true;        break;
                default: System.out.println("  [Error] Opción no válida.");
            }
        }
    }

    private void listAllEmployees() {
        List<Employee> all = employeeManager.getAllEmployees();
        if (all.isEmpty()) {
            System.out.println("\n  No hay empleados registrados.\n");
            return;
        }
        System.out.println("\n  --- Lista de Empleados (" + all.size() + ") ---");
        for (int i = 0; i < all.size(); i++) {
            System.out.println("\n  [" + (i + 1) + "] ID: " + all.get(i).getId());
            System.out.println("  " + all.get(i).toString().replace("\n", "\n  "));
        }
        System.out.println();
    }

    private void searchEmployee() {
        if (employeeManager.getEmployeeCount() == 0) {
            System.out.println("\n  No hay empleados registrados.\n");
            return;
        }
        System.out.print("\n  Nombre a buscar: ");
        String query = scanner.nextLine().trim().toLowerCase();

        List<Employee> all = employeeManager.getAllEmployees();
        List<Employee> hits = new java.util.ArrayList<>();

        for (Employee e : all) {
            if (e.getName().toLowerCase().contains(query)) {
                hits.add(e);
            }
        }

        if (hits.isEmpty()) {
            System.out.println("  No se encontraron empleados con ese nombre.\n");
            return;
        }

        System.out.println("\n  Resultados encontrados:");
        for (int i = 0; i < hits.size(); i++) {
            System.out.println("  [" + (i + 1) + "] " + hits.get(i).getName() + " (ID: " + hits.get(i).getId() + ")");
        }
        System.out.print("  Selecciona un resultado (0 para cancelar): ");

        int pick = readInt();
        if (pick < 1 || pick > hits.size()) return;

        employeeActions(hits.get(pick - 1).getId());
    }

    private void employeeActions(Long employeeId) {
        boolean back = false;
        while (!back) {
            Employee e = employeeManager.getEmployee(employeeId);
            if (e == null) {
                System.out.println("  [Error] El empleado fue eliminado.\n");
                back = true;
                continue;
            }

            System.out.println("\n  --- Datos del Empleado (ID: " + employeeId + ") ---");
            System.out.println("  " + e.toString().replace("\n", "\n  "));
            System.out.println("\n  ¿Qué deseas hacer?");
            System.out.println("  1. Editar nombre");
            System.out.println("  2. Editar ID de empleado");
            System.out.println("  3. Editar rol");
            System.out.println("  4. Eliminar empleado");
            System.out.println("  0. Volver");
            System.out.print("  Opción: ");

            switch (readInt()) {
                case 1:
                    System.out.print("  Nuevo nombre: ");
                    if (employeeManager.updateEmployeeName(employeeId, scanner.nextLine()))
                        System.out.println("  ✓ Nombre actualizado.");
                    break;
                case 2:
                    System.out.print("  Nuevo ID (solo letras y números, máximo 10 caracteres): ");
                    if (employeeManager.updateEmployeeId(employeeId, scanner.nextLine()))
                        System.out.println("  ✓ ID actualizado.");
                    break;
                case 3:
                    System.out.print("  Nuevo rol: ");
                    if (employeeManager.updateEmployeeRole(employeeId, scanner.nextLine()))
                        System.out.println("  ✓ Rol actualizado.");
                    break;
                case 4:
                    System.out.print("  ¿Confirmas eliminar este empleado? (s/n): ");
                    if (scanner.nextLine().trim().equalsIgnoreCase("s")) {
                        if (employeeManager.removeEmployee(employeeId)) {
                            System.out.println("  ✓ Empleado eliminado.");
                            back = true;
                        }
                    }
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("  [Error] Opción no válida.");
            }
        }
    }

    private void addEmployee() {
        System.out.println("\n  --- Agregar Nuevo Empleado ---");
        System.out.print("  Nombre: ");
        String name = scanner.nextLine();
        System.out.print("  ID de empleado (solo letras y números, máximo 10 caracteres): ");
        String id = scanner.nextLine();
        System.out.print("  Rol: ");
        String role = scanner.nextLine();

        if (employeeManager.addEmployee(name, id, role)) {
            System.out.println("  ✓ ¡Empleado agregado correctamente!\n");
        }
    }

    // =========================================================
    //  MENÚ DE SERVICIOS DE LIMPIEZA
    // =========================================================

    private void serviceMenu() {
        boolean back = false;
        while (!back) {
            printDivider("-");
            System.out.println("  MENÚ - SERVICIOS DE LIMPIEZA");
            printDivider("-");
            System.out.println("  1. Listar todos los servicios");
            System.out.println("  2. Filtrar servicios por estado");
            System.out.println("  3. Buscar y editar servicio");
            System.out.println("  4. Agregar nuevo servicio");
            System.out.println("  0. Volver al menú principal");
            System.out.print("  Opción: ");

            switch (readInt()) {
                case 1: listAllServices(); break;
                case 2: filterServiceByStatus(); break;
                case 3: searchService(); break;
                case 4: addService();      break;
                case 0: back = true;       break;
                default: System.out.println("  [Error] Opción no válida.");
            }
        }
    }

    private void listAllServices() {
        List<CleaningService> all = serviceManager.getAllServices();
        if (all.isEmpty()) {
            System.out.println("\n  No hay servicios registrados.\n");
            return;
        }
        System.out.println("\n  --- Lista de Servicios (" + all.size() + ") ---");
        for (int i = 0; i < all.size(); i++) {
            System.out.println("\n  [" + (i + 1) + "] ID: " + all.get(i).getId());
            System.out.println("  " + all.get(i).toString().replace("\n", "\n  "));
        }
        System.out.println();
    }

    private void filterServiceByStatus() {
        System.out.println("\n  --- Filtrar Servicios por Estado ---");
        System.out.println("  1. Pendiente");
        System.out.println("  2. En labor");
        System.out.println("  3. Finalizado");
        System.out.println("  4. Cancelado");
        System.out.print("  Selecciona un estado: ");

        ServiceStatus status = ServiceStatus.fromChoice(readInt());
        if (status == null) {
            System.out.println("  [Error] Opción no válida.\n");
            return;
        }

        List<CleaningService> filtered = serviceManager.getServicesByStatus(status);
        if (filtered.isEmpty()) {
            System.out.println("\n  No hay servicios con estado: " + status.getLabel() + "\n");
            return;
        }

        System.out.println("\n  --- Servicios con estado: " + status.getLabel() + " (" + filtered.size() + ") ---");
        for (int i = 0; i < filtered.size(); i++) {
            System.out.println("\n  [" + (i + 1) + "] ID: " + filtered.get(i).getId());
            System.out.println("  " + filtered.get(i).toString().replace("\n", "\n  "));
        }
        System.out.println();
    }

    private void searchService() {
        if (serviceManager.getServiceCount() == 0) {
            System.out.println("\n  No hay servicios registrados.\n");
            return;
        }
        System.out.print("\n  Nombre del cliente a buscar: ");
        String query = scanner.nextLine().trim().toLowerCase();

        List<CleaningService> all = serviceManager.getAllServices();
        List<CleaningService> hits = new java.util.ArrayList<>();

        for (CleaningService s : all) {
            if (s.getClientName().toLowerCase().contains(query)) {
                hits.add(s);
            }
        }

        if (hits.isEmpty()) {
            System.out.println("  No se encontraron servicios con ese cliente.\n");
            return;
        }

        System.out.println("\n  Resultados encontrados:");
        for (int i = 0; i < hits.size(); i++) {
            System.out.println("  [" + (i + 1) + "] Cliente: " + hits.get(i).getClientName() +
                             " - Estado: " + hits.get(i).getStatus().getLabel() + " (ID: " + hits.get(i).getId() + ")");
        }
        System.out.print("  Selecciona un resultado (0 para cancelar): ");

        int pick = readInt();
        if (pick < 1 || pick > hits.size()) return;

        serviceActions(hits.get(pick - 1).getId());
    }

    private void serviceActions(Long serviceId) {
        boolean back = false;
        while (!back) {
            CleaningService s = serviceManager.getService(serviceId);
            if (s == null) {
                System.out.println("  [Error] El servicio fue eliminado.\n");
                back = true;
                continue;
            }

            System.out.println("\n  --- Datos del Servicio (ID: " + serviceId + ") ---");
            System.out.println("  " + s.toString().replace("\n", "\n  "));
            System.out.println("\n  ¿Qué deseas hacer?");
            System.out.println("  1. Cambiar estado del servicio");
            System.out.println("  2. Editar fecha");
            System.out.println("  3. Editar costo");
            System.out.println("  4. Eliminar servicio");
            System.out.println("  0. Volver");
            System.out.print("  Opción: ");

            switch (readInt()) {
                case 1:
                    changeServiceStatus(serviceId);
                    break;
                case 2:
                    System.out.print("  Nueva fecha y hora (formato: dd/MM/yyyy HH:mm): ");
                    try {
                        LocalDateTime newDate = LocalDateTime.parse(scanner.nextLine(), dateFormatter);
                        if (serviceManager.updateServiceDate(serviceId, newDate))
                            System.out.println("  ✓ Fecha actualizada.");
                    } catch (Exception e) {
                        System.out.println("  [Error] Formato de fecha inválido.");
                    }
                    break;
                case 3:
                    System.out.print("  Nuevo costo: ");
                    if (serviceManager.updateServiceCost(serviceId, readDouble()))
                        System.out.println("  ✓ Costo actualizado.");
                    break;
                case 4:
                    System.out.print("  ¿Confirmas eliminar este servicio? (s/n): ");
                    if (scanner.nextLine().trim().equalsIgnoreCase("s")) {
                        if (serviceManager.removeService(serviceId)) {
                            System.out.println("  ✓ Servicio eliminado.");
                            back = true;
                        }
                    }
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("  [Error] Opción no válida.");
            }
        }
    }

    private void changeServiceStatus(Long serviceId) {
        System.out.println("\n  --- Cambiar Estado del Servicio ---");
        System.out.println("  1. Pendiente");
        System.out.println("  2. En labor");
        System.out.println("  3. Finalizado");
        System.out.println("  4. Cancelado");
        System.out.print("  Selecciona el nuevo estado: ");

        ServiceStatus newStatus = ServiceStatus.fromChoice(readInt());
        if (newStatus == null) {
            System.out.println("  [Error] Opción no válida.");
            return;
        }

        if (serviceManager.updateServiceStatus(serviceId, newStatus))
            System.out.println("  ✓ Estado actualizado a: " + newStatus.getLabel());
    }

    private void addService() {
        System.out.println("\n  --- Agregar Nuevo Servicio ---");
        System.out.print("  Nombre del cliente: ");
        String clientName = scanner.nextLine();

        List<Employee> all = employeeManager.getAllEmployees();
        if (all.isEmpty()) {
            System.out.println("  [Error] No hay empleados registrados. Crea al menos uno.\n");
            return;
        }

        System.out.println("\n  Empleados disponibles:");
        for (int i = 0; i < all.size(); i++) {
            System.out.println("  [" + (i + 1) + "] " + all.get(i).getName() + " (ID: " + all.get(i).getId() + ")");
        }

        List<Long> employeeIds = selectEmployeesFromList(all);
        if (employeeIds.isEmpty()) {
            System.out.println("  [Error] Debes seleccionar al menos un empleado.\n");
            return;
        }

        System.out.print("  Fecha y hora (formato: dd/MM/yyyy HH:mm): ");
        LocalDateTime scheduledDate = null;
        try {
            scheduledDate = LocalDateTime.parse(scanner.nextLine(), dateFormatter);
        } catch (Exception e) {
            System.out.println("  [Error] Formato de fecha inválido.\n");
            return;
        }

        System.out.print("  Costo del servicio: ");
        double cost = readDouble();

        if (serviceManager.addService(clientName, employeeIds, scheduledDate, cost)) {
            System.out.println("  ✓ ¡Servicio agregado correctamente!\n");
        }
    }

    private List<Long> selectEmployeesFromList(List<Employee> employees) {
        List<Long> selected = new java.util.ArrayList<>();
        boolean selecting = true;

        while (selecting) {
            System.out.print("  Selecciona un empleado (0 para terminar): ");
            int choice = readInt();

            if (choice == 0) {
                selecting = false;
            } else if (choice > 0 && choice <= employees.size()) {
                Long id = employees.get(choice - 1).getId();
                if (!selected.contains(id)) {
                    selected.add(id);
                    System.out.println("  ✓ " + employees.get(choice - 1).getName() + " agregado.");
                } else {
                    System.out.println("  [Info] Este empleado ya fue seleccionado.");
                }
            } else {
                System.out.println("  [Error] Opción no válida.");
            }
        }

        return selected;
    }

    // =========================================================
    //  UTILIDADES
    // =========================================================

    private int readInt() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private double readDouble() {
        try {
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void printDivider(String character) {
        System.out.println("  " + character.repeat(60));
    }
}
