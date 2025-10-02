package com.example.bancoli.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.bancoli.data.entity.Transaccion;
import java.util.List;

@Dao
public interface TransaccionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Transaccion transaccion);

    @Update
    void update(Transaccion transaccion);

    @Delete
    void delete(Transaccion transaccion);

    @Query("SELECT * FROM transacciones WHERE fkCuentaId = :cuentaId ORDER BY fechaHora DESC")
    List<Transaccion> getTransaccionesByCuentaId(long cuentaId);

    @Query("SELECT * FROM transacciones WHERE transaccionId = :id")
    Transaccion getTransaccionById(long id);

    @Query("SELECT * FROM transacciones ORDER BY fechaHora DESC")
    List<Transaccion> getAllTransacciones();
}
