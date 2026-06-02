# Servicio de Limpieza - Mini Proyecto CRUD en Java

Proyecto de curso que implementa un CRUD para gestionar clientes,
empleados y servicios de limpieza, siguiendo los principios SOLID.

---

## Estructura del Proyecto

```
src/
├── Main.java                          ← Punto de entrada
├── enums/
│   └── ServiceStatus.java             ← Estados del servicio (enum)
├── models/
│   ├── Person.java                    ← Clase base abstracta
│   ├── Client.java                    ← Hereda de Person
│   ├── Employee.java                  ← Hereda de Person
│   └── CleaningService.java           ← Modelo del servicio
├── interfaces/
│   ├── CleaningServiceRepository.java ← Conexión con base de datos
│   ├── ClientRepository.java          ← Conexión con base de datos
│   ├── EmployeeRepository.java        ← Conexión con base de datos
│   ├── IClientManager.java            ← Contrato para gestión de clientes
    ├── IEmployeeManager.java          ← Contrato para gestión de empleados
│   └── ICleaningServiceManager.java   ← Contrato para gestión de servicios
├── managers/
│   ├── ClientManager.java             ← CRUD + validación de clientes
│   ├── EmployeeManager.java           ← CRUD + validación de empleados
│   └── CleaningServiceManager.java    ← CRUD + validación de servicios
└── controllers/
│     |── ClientController.java 
│     |── EmployeeController.java
│     └── ServiceController.java       ← Controladores Web mediante SpringBoot
└──  resources/           
      ├── app.css           
      ├── app.js      
      └── templates (.html)            ← Implementaciones UI Web
  





```

---

## Principios SOLID Aplicados

### S — Single Responsibility (Responsabilidad Única)
Cada clase tiene una única razón para cambiar:
- `Client`, `Employee`, `CleaningService` → solo representan datos.
- `ClientManager`, `EmployeeManager`, `CleaningServiceManager` → solo gestionan y validan sus objetos.
- `SystemController` → solo maneja la interacción con el usuario (menús, búsquedas desde consola, opcionales a la aplicación principal).
- `Main` → solo inicializa la aplicación.

### O — Open/Closed (Abierto/Cerrado)
- `Person` es abstracta: se puede crear nuevas clases hijas (ej. `Supplier`)
  sin modificar `Person`.
- Los managers implementan interfaces, entonces agregar un nuevo tipo de manager
  no requiere modificar los existentes.

### L — Liskov Substitution (Sustitución de Liskov)
- `Client` y `Employee` extienden `Person` correctamente.
  Cualquier lugar que espere un `Person` puede recibir un `Client` o `Employee`
  sin romper el comportamiento.

### I — Interface Segregation (Segregación de Interfaces)
- `IClientManager`, `IEmployeeManager` y `ICleaningServiceManager` son
  interfaces separadas y específicas.
  Ninguna mezcla responsabilidades de las otras.

### D — Dependency Inversion (Inversión de Dependencias)
- los controladores reciben `IClientManager`, `IEmployeeManager` e
  `ICleaningServiceManager` (interfaces), no las clases concretas.




## Validaciones Implementadas

| Campo          | Regla                                          |
|----------------|------------------------------------------------|
| Nombre         | No vacío, máximo 50 caracteres                 |
| Contacto       | Solo dígitos, entre 7 y 15 caracteres          |
| Dirección      | No vacía, máximo 100 caracteres                |
| ID Empleado    | Alfanumérico, máximo 10 caracteres, único      |
| Rol Empleado   | No vacío, máximo 30 caracteres                 |
| Fecha servicio | Formato DD/MM/AAAA                             |
| Costo          | Número mayor a cero                            |
| Empleados      | Al menos uno asignado por servicio             |

---

## Pensado para Crecer

- **Base de datos**: Implementada con JPA y MySQL mediante repositorios de entidades
- **Frontend web**: Los modelos y managers ya están desacoplados de la consola,
  facilitando la integración con una API REST o un framework web.
# proyectoFinalLimpieza
