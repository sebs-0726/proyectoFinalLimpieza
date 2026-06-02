package com.limpieza.empresa.controllers;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 * Compatibility helpers to avoid class-shadowing with com.limpieza.empresa.util.ControllerUtils.
 */
public class ControllerUtils {

    public static void addFlashMessage(RedirectAttributes ra, String message, String type) {
        ra.addFlashAttribute("message", message);
        ra.addFlashAttribute("messageType", type);
    }
}
