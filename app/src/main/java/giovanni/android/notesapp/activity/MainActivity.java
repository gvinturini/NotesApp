package giovanni.android.notesapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import giovanni.android.simplenotesapp.R;
import giovanni.android.notesapp.adapter.NotaAdapter;
import giovanni.android.notesapp.database.NotaDAO;
import giovanni.android.notesapp.helper.RecyclerItemClickListener;
import giovanni.android.notesapp.model.Nota;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotaAdapter notaAdapter;
    private List<Nota> listaNotas = new ArrayList<>();
    private Nota notaSelecionada;

    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Configurar recyclerView e adView
        recyclerView = findViewById(R.id.recyclerNotas);
        adView = findViewById(R.id.adView1);

        //inicializarAd(adView);
        MobileAds.initialize(getApplicationContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        carregarNotas();

        //Adicionar evento de clique
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                getApplicationContext(),
                recyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        //Recuperar nota para edição
                        Nota notaSelecionada = listaNotas.get(position);

                        //Envia nota para a tela de criar/editar nota
                        Intent i = new Intent(MainActivity.this, NovaNotaActivity.class);
                        i.putExtra("notaSelecionada", notaSelecionada);

                        startActivity(i);

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                        notaSelecionada = listaNotas.get(position);

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                        builder.setTitle("Confirmar exclusão");
                        builder.setMessage("Deseja excluir a nota: " + notaSelecionada.getTituloNota() + " ?");

                        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                NotaDAO notaDAO = new NotaDAO(getApplicationContext());
                                if (notaDAO.deletar(notaSelecionada)) {
                                    carregarNotas();
                                    Toast.makeText(MainActivity.this, "Sucesso ao deletar nota!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "Erro ao deletar tarefa", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                        builder.setNegativeButton("Não", null);

                        builder.create();
                        builder.show();

                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }
        ));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                startActivity(new Intent(MainActivity.this, NovaNotaActivity.class));
            }
        });
    }

    private void inicializarAd(final AdView adView){
        MobileAds.initialize(getApplicationContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    public void carregarNotas(){

        //Listar notas
        NotaDAO notaDAO = new NotaDAO(getApplicationContext());
        listaNotas = notaDAO.listar();

        //Configurar adapter
        notaAdapter = new NotaAdapter(listaNotas);

        //Configurar recyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        recyclerView.setAdapter(notaAdapter);

    }

    @Override
    protected void onStart() {
        carregarNotas();
        super.onStart();
    }
}
