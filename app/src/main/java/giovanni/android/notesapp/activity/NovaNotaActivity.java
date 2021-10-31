package giovanni.android.notesapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

import giovanni.android.simplenotesapp.R;
import giovanni.android.notesapp.database.DbHelper;
import giovanni.android.notesapp.database.NotaDAO;
import giovanni.android.notesapp.model.Nota;

public class NovaNotaActivity extends AppCompatActivity {

    private Button btSalvar;
    private ImageButton btMaior, btMenor;
    private EditText textoNota, titulo;
    private TextView data;
    private float tamanhoTextoNota;

    private Calendar calendar;
    private java.text.SimpleDateFormat dateFormat;
    private String date;

    private Nota notaAtual;

    private AdView adView2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_nota);
        getSupportActionBar().hide();

        initComponentes();
        inicializarAd(adView2);

        notaAtual = (Nota) getIntent().getSerializableExtra("notaSelecionada");

        if (notaAtual != null){
            titulo.setText(notaAtual.getTituloNota());
            textoNota.setText(notaAtual.getTextoNota());
            data.setText(notaAtual.getData());
        } else {
            data.setText(definirData());
        }

        aumentarFonte(textoNota);
        diminuirFonte(textoNota);

        salvarNota();
    }

    private void salvarNota(){

        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NotaDAO notaDAO = new NotaDAO(getApplicationContext());

                if (notaAtual != null){//editar

                    String tituloNota = titulo.getText().toString();
                    String texto = textoNota.getText().toString();
                    if (!tituloNota.isEmpty() && !texto.isEmpty()){

                        Nota nota = new Nota();
                        nota.setId(notaAtual.getId());
                        nota.setTituloNota(tituloNota);
                        nota.setTextoNota(texto);
                        nota.setData(definirData());

                        if (notaDAO.atualizar(nota)){
                            finish();
                            Toast.makeText(NovaNotaActivity.this, "Nota atualizada!",
                                    Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(NovaNotaActivity.this, "Erro ao atualizar nota!",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }

                }else {//salvar

                    String tituloNota = titulo.getText().toString();
                    String texto = textoNota.getText().toString();
                    String dataAtual = data.getText().toString();
                    if (!tituloNota.isEmpty() && !texto.isEmpty()) {
                        Nota nota = new Nota();
                        nota.setTituloNota(tituloNota);
                        nota.setTextoNota(texto);
                        nota.setData(dataAtual);


                        if (notaDAO.salvar(nota)){
                            DbHelper db = new DbHelper(getApplicationContext());
                            SQLiteDatabase ler = db.getReadableDatabase();
                            String recuperarId = "SELECT ID FROM " + DbHelper.TABELA_NOTAS + " WHERE titulo = '"
                                    + tituloNota + "' " + "AND textoNota = '" + texto + "'" +" ;";
                            Cursor c = ler.rawQuery(recuperarId, null);
                            if(c.moveToFirst()){
                                nota.setId(c.getLong(c.getColumnIndex("id")));
                            }

                            finish();
                            Toast.makeText(NovaNotaActivity.this, "Nota salva!",
                                    Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(NovaNotaActivity.this, "Erro ao salvar nota!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                }

            }
        });

    }

    private void aumentarFonte(final EditText texto){

        btMaior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                texto.setTextSize(TypedValue.COMPLEX_UNIT_PX, tamanhoTextoNota + 2);
                tamanhoTextoNota = texto.getTextSize();
            }
        });

    }

    private void diminuirFonte(final EditText texto){

        btMenor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                texto.setTextSize(TypedValue.COMPLEX_UNIT_PX,tamanhoTextoNota - 2);
                tamanhoTextoNota = texto.getTextSize();
            }
        });

    }

    private String definirData(){

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        date = dateFormat.format(calendar.getTime());

        return date;

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

    private void initComponentes() {
        btMaior = findViewById(R.id.btMaior);
        btMenor = findViewById(R.id.btMenor);
        textoNota = findViewById(R.id.editNota);
        titulo = findViewById(R.id.editTitulo);
        btSalvar = findViewById(R.id.btSalvar);
        data = findViewById(R.id.textData);
        adView2 = findViewById(R.id.adView2);

        tamanhoTextoNota = textoNota.getTextSize();

    }

}
