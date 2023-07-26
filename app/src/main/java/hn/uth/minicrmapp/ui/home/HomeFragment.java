package hn.uth.minicrmapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;

import hn.uth.minicrmapp.OnItemClickListener;
import hn.uth.minicrmapp.R;
import hn.uth.minicrmapp.database.Cliente;
import hn.uth.minicrmapp.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment implements OnItemClickListener<Cliente> {

    private FragmentHomeBinding binding;
    private ClientesAdapter adaptador;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        homeViewModel.getClientesDataset().observe(getViewLifecycleOwner(), clientes -> {
            if(clientes.isEmpty()){
                Snackbar.make(binding.clHome,"No hay clientes creados", Snackbar.LENGTH_LONG).show();
            }else{
                adaptador.setItems(clientes);
            }
        });

        setupRecyclerView();

        return root;
    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        binding.rvClientes.setLayoutManager(linearLayoutManager);
        binding.rvClientes.setAdapter(adaptador);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(Cliente data) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("cliente", data);
        NavController navController = Navigation.findNavController(this.getActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.navigation_clientes, bundle);
    }
}