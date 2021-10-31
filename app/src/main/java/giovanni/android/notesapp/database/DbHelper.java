package giovanni.android.notesapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {

    public static int VERSION = 1;
    public static String NOME_DB = "DB_NOTAS";
    public static String TABELA_NOTAS = "notas";


    public DbHelper(Context context) {
        super(context, NOME_DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String statement = "CREATE TABLE IF NOT EXISTS " + TABELA_NOTAS +
                " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "titulo TEXT NOT NULL, " +
                "textoNota TEXT NOT NULL, " +
                "dataCriada TEXT NOT NULL);";

        try {
            db.execSQL(statement);
            Log.i("Info DB", "Sucesso ao criar tabela");
        }catch (Exception e){
            Log.e("Info DB", "Erro ao criar tabela " + e.getMessage());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        try {
            Log.i("Info DB", "Sucesso ao criar tabela");
        }catch (Exception e){
            Log.e("Info DB", "Erro ao criar tabela " + e.getMessage());
        }

    }
}
