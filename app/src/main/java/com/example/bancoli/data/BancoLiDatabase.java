package com.example.bancoli.data;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.example.bancoli.data.dao.BeneficiarioDao;
import com.example.bancoli.data.dao.ClienteDao;
import com.example.bancoli.data.dao.CuentaDao;
import com.example.bancoli.data.dao.TransaccionDao;
import com.example.bancoli.data.entity.Beneficiario;
import com.example.bancoli.data.entity.Cliente;
import com.example.bancoli.data.entity.Cuenta;
import com.example.bancoli.data.entity.Transaccion;

@Database(entities = {Cliente.class, Cuenta.class, Transaccion.class, Beneficiario.class}, version = 1, exportSchema = false)
public abstract class BancoLiDatabase extends RoomDatabase {

    public abstract ClienteDao clienteDao();
    public abstract CuentaDao cuentaDao();
    public abstract TransaccionDao transaccionDao();
    public abstract BeneficiarioDao beneficiarioDao();

    private static volatile BancoLiDatabase INSTANCE;

    public static BancoLiDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (BancoLiDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            BancoLiDatabase.class, "banco_li_database")
                            // Considerar migraciones para versiones futuras
                            // .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                            .fallbackToDestructiveMigration() // Opción si no se manejan migraciones detalladas
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
