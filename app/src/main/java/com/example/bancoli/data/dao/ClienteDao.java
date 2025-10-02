package com.example.bancoli.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.bancoli.data.entity.Cliente;
import java.util.List;

@Dao
public interface ClienteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Cliente cliente);

    @Update
    void update(Cliente cliente);

    @Delete
    void delete(Cliente cliente);

    @Query("SELECT * FROM clientes ORDER BY apellido, nombre ASC")
    List<Cliente> getAllClientes();

    @Query("SELECT * FROM clientes WHERE clienteId = :id")
    Cliente getClienteById(long id);
}
