package giovanni.android.notesapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import giovanni.android.notesapp.model.Nota;

public class NotaDAO implements INotaDAO {

    private SQLiteDatabase escrever;
    private SQLiteDatabase ler;

    public NotaDAO(Context context) {
        DbHelper db = new DbHelper(context);
        escrever = db.getWritableDatabase();
        ler = db.getReadableDatabase();
    }

    @Override
    public boolean salvar(Nota nota) {

        ContentValues cv = new ContentValues();
        cv.put("titulo", nota.getTituloNota());
        cv.put("textoNota", nota.getTextoNota());
        cv.put("dataCriada", nota.getData());

        try {
            escrever.insert(DbHelper.TABELA_NOTAS, null, cv);
            Log.i("Info DB", "Tabela salva com sucesso");
        }catch (Exception e){
            Log.e("Info DB", "Erro ao salvar nota" + e.getMessage());
            return false;
        }

        return true;

    }

    @Override
    public boolean atualizar(Nota nota) {

        ContentValues cv = new ContentValues();
        cv.put("titulo", nota.getTituloNota());
        cv.put("textoNota", nota.getTextoNota());
        cv.put("dataCriada", nota.getData());

        try {
            String[] args = {nota.getId().toString()};
            escrever.update(DbHelper.TABELA_NOTAS, cv, "id=?", args);
        }catch (Exception e){
            Log.e("Info DB", "Erro ao atualizar nota " + e.getMessage());
            return false;
        }

        return true;

    }

    @Override
    public boolean deletar(Nota nota) {

        try {
            String[] args = {nota.getId().toString()};
            escrever.delete(DbHelper.TABELA_NOTAS, "id=?", args);
        }catch (Exception e){
            Log.e("Info DB", "Erro ao remover nota " + e.getMessage());
            return false;
        }

        return true;

    }

    @Override
    public List<Nota> listar() {

        List<Nota> notas = new ArrayList<>();

        String statement = "SELECT * FROM " + DbHelper.TABELA_NOTAS + ";";
        Cursor c = ler.rawQuery(statement, null);

        while (c.moveToNext()) {

            Nota nota = new Nota();

            Long id = c.getLong(c.getColumnIndex("id"));
            String titulo = c.getString(c.getColumnIndex("titulo"));
            String texto = c.getString(c.getColumnIndex("textoNota"));
            String data = c.getString(c.getColumnIndex("dataCriada"));

            nota.setId(id);
            nota.setTituloNota(titulo);
            nota.setTextoNota(texto);
            nota.setData(data);

            notas.add(nota);

        }

        return notas;

    }
}
