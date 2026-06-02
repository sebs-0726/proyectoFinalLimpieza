package com.limpieza.empresa.enums;

/**
 * ENUM: Estado de un servicio de limpieza.
 * Cada constante tiene una etiqueta en español para mostrar en consola.
 */
public enum ServiceStatus {

    PENDING("Pendiente"),
    IN_PROGRESS("En labor"),
    FINISHED("Finalizado"),
    CANCELLED("Cancelado");

    private final String label;

    ServiceStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    /**
     * Convierte un número (1-4) al estado correspondiente.
     * Retorna null si la opción no es válida.
     */
    public static ServiceStatus fromChoice(int choice) {
        switch (choice) {
            case 1: return PENDING;
            case 2: return IN_PROGRESS;
            case 3: return FINISHED;
            case 4: return CANCELLED;
            default: return null;
        }
    }
}

