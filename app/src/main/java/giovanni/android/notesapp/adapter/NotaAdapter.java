package giovanni.android.notesapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import giovanni.android.simplenotesapp.R;
import giovanni.android.notesapp.model.Nota;

public class NotaAdapter extends RecyclerView.Adapter<NotaAdapter.MyViewHolder> {

    private List<Nota> listaNotas;

    public NotaAdapter(List<Nota> listaNotas) {
        this.listaNotas = listaNotas;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemNota = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_nota_adapter, parent, false);

        return new MyViewHolder(itemNota);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Nota nota = listaNotas.get(position);
        holder.titulo.setText(nota.getTituloNota());
        holder.conteudo.setText(nota.getTextoNota());
        holder.data.setText(nota.getData());

    }


    @Override
    public int getItemCount() {
        return this.listaNotas.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView titulo;
        TextView conteudo;
        TextView data;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            titulo = itemView.findViewById(R.id.textAdapterTitulo);
            conteudo = itemView.findViewById(R.id.textConteudo);
            data = itemView.findViewById(R.id.textAdapterData);

        }
    }

}
