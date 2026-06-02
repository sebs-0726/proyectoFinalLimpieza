# Servicio de Limpieza - Mini Proyecto CRUD en Java

Proyecto educativo que implementa un CRUD para gestionar clientes,
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
│   ├── IClientManager.java            ← Contrato para gestión de clientes
│   ├── IEmployeeManager.java          ← Contrato para gestión de empleados
│   └── ICleaningServiceManager.java   ← Contrato para gestión de servicios
├── managers/
│   ├── ClientManager.java             ← CRUD + validación de clientes
│   ├── EmployeeManager.java           ← CRUD + validación de empleados
│   └── CleaningServiceManager.java    ← CRUD + validación de servicios
└── ui/
    └── SystemController.java          ← Menús, búsqueda y visualización
```

---

## Principios SOLID Aplicados

### S — Single Responsibility (Responsabilidad Única)
Cada clase tiene una única razón para cambiar:
- `Client`, `Employee`, `CleaningService` → solo representan datos.
- `ClientManager`, `EmployeeManager`, `CleaningServiceManager` → solo gestionan y validan sus objetos.
- `SystemController` → solo maneja la interacción con el usuario (menús, búsquedas).
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
- `SystemController` recibe `IClientManager`, `IEmployeeManager` e
  `ICleaningServiceManager` (interfaces), no las clases concretas.
- Esto significa que si en el futuro los managers guardan datos en una base de datos,
  `SystemController` no necesita ningún cambio, solo se cambia la implementación
  concreta en `Main.java`.

---

## Cómo Compilar y Ejecutar

### Requisitos
- Java JDK 8 o superior instalado.

### Compilar (desde la carpeta `src/`)
```bash
cd src
javac -encoding UTF-8 enums/*.java models/*.java interfaces/*.java managers/*.java ui/*.java Main.java
```

### Ejecutar
```bash
java Main
```

---

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

- **Base de datos**: Solo se reemplaza la implementación de los managers
  (ej. `ClientManagerDB`). `SystemController` y los modelos no cambian.
- **Frontend web**: Los modelos y managers ya están desacoplados de la consola,
  facilitando la integración con una API REST o un framework web.
# final-tecnicas-de-programacion
