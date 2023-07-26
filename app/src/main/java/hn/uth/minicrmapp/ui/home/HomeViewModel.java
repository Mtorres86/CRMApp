package hn.uth.minicrmapp.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import hn.uth.minicrmapp.database.Cliente;
import hn.uth.minicrmapp.database.ClientesRepository;

public class HomeViewModel extends AndroidViewModel {

    private ClientesRepository repository;
    private final LiveData<List<Cliente>> clientesDataset;

    public HomeViewModel(@NonNull Application app) {
        super(app);
        this.repository = new ClientesRepository(app);
        this.clientesDataset = repository.getDataset();
    }

    public ClientesRepository getRepository() {
        return repository;
    }

    public LiveData<List<Cliente>> getClientesDataset() {
        return clientesDataset;
    }

    public void insert(Cliente nuevo){
        repository.insert(nuevo);
    }

    public void update(Cliente actualizar){
        repository.update(actualizar);
    }

    public void delete(Cliente eliminar){
        repository.delete(eliminar);
    }

    public void deleteAll(){
        repository.deleteAll();
    }
}