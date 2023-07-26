package hn.uth.minicrmapp.ui.crm;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.snackbar.SnackbarContentLayout;

import java.util.ArrayList;
import java.util.List;

import hn.uth.minicrmapp.databinding.FragmentClientesBinding;
import hn.uth.minicrmapp.entity.Contacto;

public class ClientesFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int PERMISSION_REQUEST_READ_CONTACT = 700;

    private FragmentClientesBinding binding;

    private boolean isPermissionGranted = false;


    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                    unblockFields();
                } else {
                    blockFields();
                }
            });


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ClientesViewModel dashboardViewModel =
                new ViewModelProvider(this).get(ClientesViewModel.class);

        binding = FragmentClientesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        binding.textContact.addTextChangedListener(textWatcherContact);





        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.READ_CONTACTS) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.

        } else if (shouldShowRequestPermissionRationale(...)) {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected, and what
            // features are disabled if it's declined. In this UI, include a
            // "cancel" or "no thanks" button that lets the user continue
            // using your app without granting the permission.
            showInContextUI(...);
        } else {
            // You can directly ask for the permission.
            requestPermissions(CONTEXT,
                    new String[] { Manifest.permission.REQUESTED_PERMISSION },
                    REQUEST_CODE);
        }


        // Verificar permisos al crear el fragmento
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            // Permiso otorgado, configurar la variable de permiso a true
            isPermissionGranted = true;
            unblockFields();
        } else {
            // Permiso no otorgado, solicitarlo al usuario
            blockFields();
            requestReadContactsPermission();
        }



        return root;
    }

    TextWatcher textWatcherContact = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String searchText = s.toString();
            if (searchText.length()>=4){
                searchData(searchText);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    public void searchData(String textSearchContact){
        //if(binding.tilSearch.getEditText() != null && binding.tilSearch.getEditText().getText() != null && !binding.tilSearch.getEditText().toString().isEmpty()){
            String busqueda = textSearchContact;
            if(busqueda.length() >= 4){
                //solicitarPermisoContactos(this.getContext());
                List<Contacto> contactos = getContacts(this.getContext(), textSearchContact);
                mostrarContacto(contactos, false, true);
            }else{
                Snackbar.make(binding.getRoot(), "Debes de escribir un nombre de mínimo 4 caracteres", Snackbar.LENGTH_LONG).show();
            }
        //}else{
        //    Snackbar.make(binding.getRoot(), "Debes de escribir un nombre para realizar la búsqueda", Snackbar.LENGTH_LONG).show();
        //}
    }

    private boolean solicitarPermisoContactos(Context context){

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // Si el permiso no está otorgado, solicitarlo al usuario
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, PERMISSION_REQUEST_READ_CONTACT);
            return true;
        }
        //PREGUNTANDO SI YA TENGO UN DETERMINADO PERMISO
        //if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
        //    //ENTRA AQUI SI NO ME HAN DADO EL PERMISO, Y DEBO DE SOLICITARLO
        //    ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, PERMISSION_REQUEST_READ_CONTACT);
        //    return true;
        //}
        /*else{
            //ENTRA AQUI SI EL USUARIO YA ME OTORGÓ EL PERMISO ANTES, PUEDO HACER USO DE LA LECTURA DE CONTACTOS
            return getContacts(context, searchText);
            Snackbar.
        }*/
        return false;
    }

    private List<Contacto> getContacts(Context context, String textSearch) {
        List<Contacto> contactos = new ArrayList<>();

        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI,
                null, ContactsContract.Contacts.DISPLAY_NAME + " LIKE '%"+textSearch+"%'", null, ContactsContract.Contacts.DISPLAY_NAME + " DESC");

        if(cursor.getCount() > 0){
            while (cursor.moveToNext()){
                int idColumnIndex = Math.max(cursor.getColumnIndex(ContactsContract.Contacts._ID), 0);
                int nameColumnIndex = Math.max(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME), 0);
                int phoneColumnIndex = Math.max(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER), 0);//ME DICE SI TIENE O NO UN TELEFONO GUARDADO

                String id = cursor.getString(idColumnIndex);
                String name = cursor.getString(nameColumnIndex);

                if(Integer.parseInt(cursor.getString(phoneColumnIndex)) > 0){
                    //EL CONTACTO SI TIENE TELEFONO ALMACENADO
                    Cursor cursorPhone = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", new String[]{id}, null);

                    while (cursorPhone.moveToNext()){
                        int phoneCommonColumIndex = cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        String phone = cursorPhone.getString(phoneCommonColumIndex);

                        Contacto nuevo = new Contacto();
                        nuevo.setName(name);
                        nuevo.setPhone(phone);
                        //nuevo.setEmail(""); ME FALTA BUSCAR ESTE

                        contactos.add(nuevo);
                    }
                    cursorPhone.close();
                }
            }
            cursor.close();
        }

        return contactos;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_READ_CONTACT:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permiso otorgado, configurar la variable de permiso a true
                    isPermissionGranted = true;
                    unblockFields();
                } else {
                    // Permiso denegado, mostrar un mensaje o realizar alguna acción apropiada
                    isPermissionGranted = false;
                    Snackbar.make(binding.getRoot(), "No se pueden buscar contactos", Snackbar.LENGTH_LONG).show();
                    blockFields();

                    boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_CONTACTS);
                    if (!showRationale) {
                        showPermissionInfo();
                    }
                }
        }
        // Resto del código del fragmento ...

            // Resto del código del fragmento ...



    }


    public void blockFields(){
        binding.textContact.setText("");
        binding.textContact.setFocusable(false);
        binding.textContact.setEnabled(false);
    }

    public void unblockFields(){
        binding.textContact.setFocusable(true);
        binding.textContact.setEnabled(true);
    }

    private void mostrarContacto(List<Contacto> contactos, boolean mostrarMensajeExito, boolean MostrarMensajeError) {
        if(contactos.isEmpty()){
            if(MostrarMensajeError){
                Snackbar.make(binding.getRoot(), "No hay coincidencias en la búsqueda", Snackbar.LENGTH_LONG).show();
            }
        }else{
            binding.tilNombre.getEditText().setText(contactos.get(0).getName());
            binding.tilTelefono.getEditText().setText(contactos.get(0).getPhone());
            Snackbar.make(binding.getRoot(), contactos.size() + " Contactos encontrados", Snackbar.LENGTH_LONG).show();
            if(mostrarMensajeExito){
                Snackbar.make(binding.getRoot(), "Contacto cargado", Snackbar.LENGTH_LONG).show();
            }
        }
    }


    private void requestReadContactsPermission() {
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_CONTACTS}, PERMISSION_REQUEST_READ_CONTACT);
    }

    private void showPermissionInfo() {
        Toast.makeText(requireContext(), "El permiso es requerido", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", requireContext().getPackageName(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}