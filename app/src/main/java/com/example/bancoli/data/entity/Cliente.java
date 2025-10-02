package com.example.bancoli.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "clientes")
public class Cliente {

    @PrimaryKey(autoGenerate = true)
    public long clienteId;

    public String nombre;
    public String apellido;
    public String direccion;
    public String telefono;
    public long fechaRegistro; // Timestamp

    // Constructor, getters y setters (opcionalmente generados por Lombok o manualmente)

    public Cliente(String nombre, String apellido, String direccion, String telefono, long fechaRegistro) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.direccion = direccion;
        this.telefono = telefono;
        this.fechaRegistro = fechaRegistro;
    }

    // Getters y Setters (puedes generarlos con el IDE o añadirlos si los necesitas explícitamente)
    // Por brevedad, no los incluyo todos aquí, pero en un proyecto real serían recomendables.

    public long getClienteId() {
        return clienteId;
    }

    public void setClienteId(long clienteId) {
        this.clienteId = clienteId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public long getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(long fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
