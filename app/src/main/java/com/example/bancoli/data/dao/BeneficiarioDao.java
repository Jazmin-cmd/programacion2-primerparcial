package com.example.bancoli.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.bancoli.data.entity.Beneficiario;
import java.util.List;

@Dao
public interface BeneficiarioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Beneficiario beneficiario);

    @Update
    void update(Beneficiario beneficiario);

    @Delete
    void delete(Beneficiario beneficiario);

    @Query("SELECT * FROM beneficiarios WHERE fkClienteId = :clienteId ORDER BY nombreCompletoBeneficiario ASC")
    List<Beneficiario> getBeneficiariosByClienteId(long clienteId);

    @Query("SELECT * FROM beneficiarios WHERE beneficiarioId = :id")
    Beneficiario getBeneficiarioById(long id);

    @Query("SELECT * FROM beneficiarios ORDER BY nombreCompletoBeneficiario ASC")
    List<Beneficiario> getAllBeneficiarios();
}
