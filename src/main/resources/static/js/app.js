// Mostrar/Ocultar formularios
function toggleForm(formId) {
    const form = document.getElementById(formId);
    if (form) {
        form.style.display = form.style.display === 'none' ? 'block' : 'none';
    }
}

// Limpiar todos los formularios
function clearAllForms() {
    document.querySelectorAll('.form-section').forEach(form => {
        form.style.display = 'none';
    });
}

// Validar Cliente - Búsqueda
function validateClientSearch(form) {
    const name = form.querySelector('input[name="name"]');
    if (!name.value.trim()) {
        alert('Por favor ingresa un nombre para buscar');
        return false;
    }
    return true;
}

// Validar Cliente - Crear/Editar
function validateClientForm(form) {
    const name = form.querySelector('input[name="name"]');
    const contact = form.querySelector('input[name="contact"]');
    const address = form.querySelector('textarea[name="address"]');

    if (!name.value.trim()) {
        alert('El nombre es requerido');
        return false;
    }
    if (name.value.trim().length > 50) {
        alert('El nombre no puede exceder 50 caracteres');
        return false;
    }

    if (!contact.value.trim()) {
        alert('El teléfono es requerido');
        return false;
    }
    if (!/^[0-9]{7,15}$/.test(contact.value)) {
        alert('El teléfono debe contener solo números entre 7 y 15 dígitos');
        return false;
    }

    if (!address.value.trim()) {
        alert('La dirección es requerida');
        return false;
    }
    if (address.value.trim().length > 100) {
        alert('La dirección no puede exceder 100 caracteres');
        return false;
    }

    return true;
}

// Validar Cliente - Editar
function validateEditClientForm(form) {
    const field = form.querySelector('input[name="field"]').value;
    const value = form.querySelector('input[name="value"], textarea[name="value"]');

    if (!value.value.trim()) {
        alert('El campo es requerido');
        return false;
    }

    if (field === 'name') {
        if (value.value.trim().length > 50) {
            alert('El nombre no puede exceder 50 caracteres');
            return false;
        }
    } else if (field === 'contact') {
        if (!/^[0-9]{7,15}$/.test(value.value)) {
            alert('El teléfono debe contener solo números entre 7 y 15 dígitos');
            return false;
        }
    } else if (field === 'address') {
        if (value.value.trim().length > 100) {
            alert('La dirección no puede exceder 100 caracteres');
            return false;
        }
    }

    return true;
}

// Validar Empleado - Crear/Editar
function validateEmployeeForm(form) {
    const name = form.querySelector('input[name="name"]');
    const idnt = form.querySelector('input[name="idnt"]');
    const role = form.querySelector('input[name="role"]');

    if (!name.value.trim()) {
        alert('El nombre es requerido');
        return false;
    }
    if (name.value.trim().length > 50) {
        alert('El nombre no puede exceder 50 caracteres');
        return false;
    }

    if (!idnt.value.trim()) {
        alert('El ID del empleado es requerido');
        return false;
    }
    if (!/^[A-Za-z0-9]+$/.test(idnt.value)) {
        alert('El ID solo puede contener letras y números');
        return false;
    }
    if (idnt.value.length > 10) {
        alert('El ID no puede exceder 10 caracteres');
        return false;
    }

    if (!role.value.trim()) {
        alert('El puesto es requerido');
        return false;
    }
    if (role.value.trim().length > 30) {
        alert('El puesto no puede exceder 30 caracteres');
        return false;
    }

    return true;
}

// Validar Empleado - Editar
function validateEditEmployeeForm(form) {
    const field = form.querySelector('input[name="field"]').value;
    const value = form.querySelector('input[name="value"]');

    if (!value.value.trim()) {
        alert('El campo es requerido');
        return false;
    }

    if (field === 'name') {
        if (value.value.trim().length > 50) {
            alert('El nombre no puede exceder 50 caracteres');
            return false;
        }
    } else if (field === 'idnt') {
        if (!/^[A-Za-z0-9]+$/.test(value.value)) {
            alert('El ID solo puede contener letras y números');
            return false;
        }
        if (value.value.length > 10) {
            alert('El ID no puede exceder 10 caracteres');
            return false;
        }
    } else if (field === 'role') {
        if (value.value.trim().length > 30) {
            alert('El puesto no puede exceder 30 caracteres');
            return false;
        }
    }

    return true;
}

// Validar Servicio - Búsqueda
function validateServiceSearch(form) {
    const clientName = form.querySelector('input[name="clientName"]');
    if (!clientName.value.trim()) {
        alert('Por favor ingresa un nombre de cliente para buscar');
        return false;
    }
    return true;
}

// Validar Servicio - Crear
function validateServiceForm(form) {
    const clientName = form.querySelector('input[name="clientName"]');
    const employeeIds = form.querySelectorAll('input[name="employeeIds"]:checked');
    const scheduledDate = form.querySelector('input[name="scheduledDate"]');
    const cost = form.querySelector('input[name="cost"]');

    if (!clientName.value.trim()) {
        alert('El nombre del cliente es requerido');
        return false;
    }
    if (clientName.value.trim().length > 50) {
        alert('El nombre del cliente no puede exceder 50 caracteres');
        return false;
    }

    if (employeeIds.length === 0) {
        alert('Debe seleccionar al menos un empleado');
        return false;
    }

    if (!scheduledDate.value.trim()) {
        alert('La fecha y hora son requeridas');
        return false;
    }
    if (!isValidDateTime(scheduledDate.value)) {
        alert('Formato de fecha inválido. Use: dd/MM/yyyy HH:mm (ej: 15/06/2026 14:30)');
        return false;
    }

    if (!cost.value) {
        alert('El costo es requerido');
        return false;
    }
    const costValue = parseFloat(cost.value);
    if (isNaN(costValue) || costValue <= 0) {
        alert('El costo debe ser un número mayor a 0');
        return false;
    }

    return true;
}

// Validar formato de fecha dd/MM/yyyy HH:mm
function isValidDateTime(dateStr) {
    const regex = /^(\d{2})\/(\d{2})\/(\d{4}) (\d{2}):(\d{2})$/;
    const match = dateStr.match(regex);

    if (!match) return false;

    const day = parseInt(match[1]);
    const month = parseInt(match[2]);
    const year = parseInt(match[3]);
    const hour = parseInt(match[4]);
    const minute = parseInt(match[5]);

    if (day < 1 || day > 31) return false;
    if (month < 1 || month > 12) return false;
    if (year < 2000 || year > 2100) return false;
    if (hour < 0 || hour > 23) return false;
    if (minute < 0 || minute > 59) return false;

    return true;
}

// Inicializar al cargar la página
document.addEventListener('DOMContentLoaded', function() {
    // Cerrar formularios abiertos si el usuario hace click fuera
    document.addEventListener('click', function(event) {
        if (!event.target.closest('.form-section') && !event.target.closest('button')) {
            // No cerrar automáticamente para evitar interrumpir el usuario
        }
    });

    // Validar inputs en tiempo real
    document.querySelectorAll('input[name="contact"]').forEach(input => {
        input.addEventListener('input', function(e) {
            e.target.value = e.target.value.replace(/[^0-9]/g, '');
        });
    });

    document.querySelectorAll('input[name="idnt"]').forEach(input => {
        input.addEventListener('input', function(e) {
            e.target.value = e.target.value.replace(/[^A-Za-z0-9]/g, '');
        });
    });

    document.querySelectorAll('input[name="cost"]').forEach(input => {
        input.addEventListener('change', function(e) {
            const value = parseFloat(e.target.value);
            if (value <= 0) {
                e.target.value = '';
            }
        });
    });
});
