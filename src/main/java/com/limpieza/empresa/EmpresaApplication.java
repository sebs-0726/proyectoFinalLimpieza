package com.limpieza.empresa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;

import com.limpieza.empresa.ui.SystemController;

@SpringBootApplication
public class EmpresaApplication implements CommandLineRunner {

	@Autowired(required = false)
	private SystemController systemController;

	public static void main(String[] args) {
		SpringApplication.run(EmpresaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (systemController == null) {
			// CLI controller not active (web-only). Skip starting console UI.
			return;
		}
		Thread t = new Thread(() -> {
			try {
				systemController.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}, "system-controller");
		// make it a daemon so the JVM can exit if only this thread remains
		t.setDaemon(true);
		t.start();
	}
}