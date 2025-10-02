package com.example.bancoli.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "transacciones",
        foreignKeys = @ForeignKey(entity = Cuenta.class,
                                  parentColumns = "cuentaId",
                                  childColumns = "fkCuentaId",
                                  onDelete = ForeignKey.CASCADE),
        indices = {@Index(value = "fkCuentaId")})
public class Transaccion {

    @PrimaryKey(autoGenerate = true)
    public long transaccionId;

    public long fkCuentaId;
    public String tipoTransaccion; // "Depósito", "Retiro", "Transferencia Saliente", "Transferencia Entrante"
    public double monto;
    public long fechaHora; // Timestamp
    public String descripcion; // Opcional

    public Transaccion(long fkCuentaId, String tipoTransaccion, double monto, long fechaHora, String descripcion) {
        this.fkCuentaId = fkCuentaId;
        this.tipoTransaccion = tipoTransaccion;
        this.monto = monto;
        this.fechaHora = fechaHora;
        this.descripcion = descripcion;
    }

    // Getters y Setters (opcional)
    public long getTransaccionId() {
        return transaccionId;
    }

    public void setTransaccionId(long transaccionId) {
        this.transaccionId = transaccionId;
    }

    public long getFkCuentaId() {
        return fkCuentaId;
    }

    public void setFkCuentaId(long fkCuentaId) {
        this.fkCuentaId = fkCuentaId;
    }

    public String getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setTipoTransaccion(String tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public long getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(long fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
