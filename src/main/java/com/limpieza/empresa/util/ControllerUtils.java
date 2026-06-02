package com.limpieza.empresa.util;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility helpers for controllers (shared DateTimeFormatter and flash helpers).
 */
public final class ControllerUtils {

    private ControllerUtils() {}

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static void addFlashMessage(RedirectAttributes ra, String message, String type) {
        ra.addFlashAttribute("message", message);
        ra.addFlashAttribute("messageType", type);
    }

    public static LocalDateTime parseDate(String value) {
        return LocalDateTime.parse(value, DATE_FORMATTER);
    }
}
