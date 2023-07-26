package hn.uth.minicrmapp.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(version = 1, exportSchema = false, entities = {Cliente.class})
public abstract class ClientesDatabase extends RoomDatabase {
    public abstract ClientesDao pedidoDao();

    private static volatile ClientesDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    //GENERANDO UNA INSTANCIA MEDIANTE PATRÓN DE SOFTWARE SINGLETON
    static ClientesDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (ClientesDatabase.class){
                if(INSTANCE == null){

                    Callback miCallback = new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);

                            databaseWriteExecutor.execute(() -> {
                                ClientesDao dao = INSTANCE.pedidoDao();
                                dao.deleteAll();
                            });

                        }
                    };
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ClientesDatabase.class, "clientes_db").addCallback(miCallback).build();
                }
            }
        }
        return INSTANCE;
    }
}
