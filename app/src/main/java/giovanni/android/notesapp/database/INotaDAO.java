package giovanni.android.notesapp.database;

import java.util.List;

import giovanni.android.notesapp.model.Nota;

public interface INotaDAO {

    public boolean salvar(Nota nota);
    public boolean atualizar(Nota nota);
    public boolean deletar(Nota nota);
    public List<Nota> listar();

}
