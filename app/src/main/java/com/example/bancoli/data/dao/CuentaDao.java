package com.example.bancoli.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.bancoli.data.entity.Cuenta;
import java.util.List;

@Dao
public interface CuentaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Cuenta cuenta);

    @Update
    void update(Cuenta cuenta);

    @Delete
    void delete(Cuenta cuenta);

    @Query("SELECT * FROM cuentas ORDER BY numeroCuenta ASC")
    List<Cuenta> getAllCuentas();

    @Query("SELECT * FROM cuentas WHERE cuentaId = :id")
    Cuenta getCuentaById(long id);

    @Query("SELECT * FROM cuentas WHERE fkClienteId = :clienteId ORDER BY numeroCuenta ASC")
    List<Cuenta> getCuentasByClienteId(long clienteId);

    @Query("SELECT * FROM cuentas WHERE numeroCuenta = :numeroCuenta LIMIT 1")
    Cuenta getCuentaByNumeroCuenta(String numeroCuenta);
}
