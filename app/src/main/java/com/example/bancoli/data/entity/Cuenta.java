package com.example.bancoli.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "cuentas",
        foreignKeys = @ForeignKey(entity = Cliente.class,
                                  parentColumns = "clienteId",
                                  childColumns = "fkClienteId",
                                  onDelete = ForeignKey.CASCADE), // Define la acción en cascada si se borra un cliente
        indices = {@Index(value = "fkClienteId"), @Index(value = "numeroCuenta", unique = true)}) // Índice para fk y unicidad de numeroCuenta
public class Cuenta {

    @PrimaryKey(autoGenerate = true)
    public long cuentaId;

    public long fkClienteId;
    public String numeroCuenta;
    public String tipoCuenta; // "Ahorro", "Corriente", etc.
    public double saldo;
    public long fechaApertura; // Timestamp

    public Cuenta(long fkClienteId, String numeroCuenta, String tipoCuenta, double saldo, long fechaApertura) {
        this.fkClienteId = fkClienteId;
        this.numeroCuenta = numeroCuenta;
        this.tipoCuenta = tipoCuenta;
        this.saldo = saldo;
        this.fechaApertura = fechaApertura;
    }

    // Getters y Setters (opcional)
    // Por ejemplo:
    public long getCuentaId() {
        return cuentaId;
    }

    public void setCuentaId(long cuentaId) {
        this.cuentaId = cuentaId;
    }

    public long getFkClienteId() {
        return fkClienteId;
    }

    public void setFkClienteId(long fkClienteId) {
        this.fkClienteId = fkClienteId;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public long getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(long fechaApertura) {
        this.fechaApertura = fechaApertura;
    }
}
