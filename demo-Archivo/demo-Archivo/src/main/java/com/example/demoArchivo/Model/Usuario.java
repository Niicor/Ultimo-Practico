
package com.example.demoArchivo.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private Long id;
    private String correo;
    private String contrasenia;
    private String nombre;
    private String nombre_rol;
    private boolean permiso_crear;
    private boolean permiso_modificar;
    private boolean permiso_eliminar;
    private boolean permiso_descargar;

    public Usuario() {
    }

    public Usuario(Long id, String correo, String contrasenia, String nombre, String nombre_rol, boolean permiso_crear, boolean permiso_modificar, boolean permiso_eliminar, boolean permiso_descargar) {
        this.id = id;
        this.correo = correo;
        this.contrasenia = contrasenia;
        this.nombre = nombre;
        this.nombre_rol = nombre_rol;
        this.permiso_crear = permiso_crear;
        this.permiso_modificar = permiso_modificar;
        this.permiso_eliminar = permiso_eliminar;
        this.permiso_descargar = permiso_descargar;
    }
    
    
}
