package hn.uth.minicrmapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ClientesDao {

    @Insert
    void insert(Cliente nuevo);

    @Update
    void update(Cliente actualizar);

    @Query("DELETE FROM clientes_table")
    void deleteAll();

    @Delete
    void delete(Cliente eliminar);

    @Query("select * from clientes_table order by nombre")
    LiveData<List<Cliente>> getClientes();
}
