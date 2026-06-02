package com.limpieza.empresa.models;
import jakarta.persistence.*;
/**
 * MODELO: Cliente.
 * Hereda el atributo 'name' de Person.
 * Agrega 'contact' (teléfono) y 'address' (dirección).
 *
 * Esta clase solo representa los datos del cliente.
 * La validación y gestión la hace ClientManager.
 */

@Entity
@Table(name = "client")
public class Client extends Person {

    private String contact;
    private String address;

    public Client(){}
    public Client(String name, String contact, String address) {
        super(name);
        this.contact = contact;
        this.address = address;
    }

    public String getContact() {return contact;}
    public void setContact(String contact) {this.contact = contact;}

    public String getAddress() {return address;}
    public void setAddress(String address) {this.address = address;}

    @Override
    public String toString() {
        return "Nombre   : " + name    + "\n" +
               "Contacto : " + contact + "\n" +
               "Dirección: " + address;
    }
}
