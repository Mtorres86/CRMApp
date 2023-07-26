package hn.uth.minicrmapp.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ClientesRepository {
    private ClientesDao dao;
    private LiveData<List<Cliente>> dataset;

    public ClientesRepository(Application app) {
        ClientesDatabase db = ClientesDatabase.getDatabase(app);
        this.dao = db.pedidoDao();
        this.dataset = dao.getClientes();
    }

    public LiveData<List<Cliente>> getDataset() {
        return dataset;
    }

    public void insert(Cliente nuevo){
        //INSERTANDO DE FORMA ASINCRONA, PARA NO AFECTAR LA INTERFAZ DE USUARIO
        ClientesDatabase.databaseWriteExecutor.execute(() -> {
            dao.insert(nuevo);
        });
    }

    public void update(Cliente actualizar){
        //ACTUALIZANDO DE FORMA ASINCRONA, PARA NO AFECTAR LA INTERFAZ DE USUARIO
        ClientesDatabase.databaseWriteExecutor.execute(() -> {
            dao.update(actualizar);
        });
    }

    public void delete(Cliente borrar){
        //BORRANDO UN REGISTRO DE FORMA ASINCRONA, PARA NO AFECTAR LA INTERFAZ DE USUARIO
        ClientesDatabase.databaseWriteExecutor.execute(() -> {
            dao.delete(borrar);
        });
    }

    public void deleteAll(){
        //BORRANDO TODOS LOS REGISTROS DE FORMA ASINCRONA, PARA NO AFECTAR LA INTERFAZ DE USUARIO
        ClientesDatabase.databaseWriteExecutor.execute(() -> {
            dao.deleteAll();
        });
    }
}
