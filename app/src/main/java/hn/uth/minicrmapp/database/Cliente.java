package hn.uth.minicrmapp.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "clientes_table")
public class Cliente implements Serializable{

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private Integer idCliente;
    @NonNull
    @ColumnInfo(name = "nombre")
    private String nombre;
    @NonNull
    @ColumnInfo(name = "telefono")
    private String telefono;

    @ColumnInfo(name = "correo")
    private String correo;

    public Cliente(@NonNull String nombre, @NonNull String telefono, String correo) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
    }

    @NonNull
    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(@NonNull Integer idCliente) {
        this.idCliente = idCliente;
    }

    @NonNull
    public String getNombre() {
        return nombre;
    }

    public void setNombre(@NonNull String nombre) {
        this.nombre = nombre;
    }

    @NonNull
    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(@NonNull String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
