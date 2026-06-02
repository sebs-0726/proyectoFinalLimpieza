package com.limpieza.empresa.models;

import jakarta.persistence.*;

/**
 * MODELO BASE: Persona.
 *
 * Clase abstracta (no se puede instanciar directamente).
 * Client y Employee heredan de esta clase.
 *
 * SOLID - L (Liskov Substitution): Cualquier objeto de tipo Client o Employee
 * puede usarse donde se espera un Person sin romper el comportamiento.
 *
 * SOLID - O (Open/Closed): Si en el futuro se agrega otro tipo de persona
 * (ej. Supplier), solo se crea una nueva clase hija sin modificar Person.
 */
@Entity
@Table(name = "person")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected String name;

    public Person() {}

    public Person(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
