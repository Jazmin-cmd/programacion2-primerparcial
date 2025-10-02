package com.example.bancoli.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "beneficiarios",
        foreignKeys = @ForeignKey(entity = Cliente.class,
                                  parentColumns = "clienteId",
                                  childColumns = "fkClienteId",
                                  onDelete = ForeignKey.CASCADE),
        indices = {@Index(value = "fkClienteId")})
public class Beneficiario {

    @PrimaryKey(autoGenerate = true)
    public long beneficiarioId;

    public long fkClienteId; // El cliente que registró este beneficiario
    public String nombreCompletoBeneficiario;
    public String numeroCuentaBeneficiario;
    public String bancoBeneficiario;

    public Beneficiario(long fkClienteId, String nombreCompletoBeneficiario, String numeroCuentaBeneficiario, String bancoBeneficiario) {
        this.fkClienteId = fkClienteId;
        this.nombreCompletoBeneficiario = nombreCompletoBeneficiario;
        this.numeroCuentaBeneficiario = numeroCuentaBeneficiario;
        this.bancoBeneficiario = bancoBeneficiario;
    }

    // Getters y Setters (opcional)
    public long getBeneficiarioId() {
        return beneficiarioId;
    }

    public void setBeneficiarioId(long beneficiarioId) {
        this.beneficiarioId = beneficiarioId;
    }

    public long getFkClienteId() {
        return fkClienteId;
    }

    public void setFkClienteId(long fkClienteId) {
        this.fkClienteId = fkClienteId;
    }

    public String getNombreCompletoBeneficiario() {
        return nombreCompletoBeneficiario;
    }

    public void setNombreCompletoBeneficiario(String nombreCompletoBeneficiario) {
        this.nombreCompletoBeneficiario = nombreCompletoBeneficiario;
    }

    public String getNumeroCuentaBeneficiario() {
        return numeroCuentaBeneficiario;
    }

    public void setNumeroCuentaBeneficiario(String numeroCuentaBeneficiario) {
        this.numeroCuentaBeneficiario = numeroCuentaBeneficiario;
    }

    public String getBancoBeneficiario() {
        return bancoBeneficiario;
    }

    public void setBancoBeneficiario(String bancoBeneficiario) {
        this.bancoBeneficiario = bancoBeneficiario;
    }
}
