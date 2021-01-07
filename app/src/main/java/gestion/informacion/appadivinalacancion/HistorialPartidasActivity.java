package gestion.informacion.appadivinalacancion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.List;

import gestion.informacion.appadivinalacancion.util.Adaptadores.ListaHistorialAdapter;
import gestion.informacion.appadivinalacancion.util.Adaptadores.ListaJugadoresAdapter;
import gestion.informacion.appadivinalacancion.util.BBDD.BBDD_Helper;
import gestion.informacion.appadivinalacancion.util.Modelo.Partida;
import gestion.informacion.appadivinalacancion.util.Otros.AppException;
import gestion.informacion.appadivinalacancion.util.Otros.SingletonMap;

public class HistorialPartidasActivity extends AppCompatActivity {

    private RecyclerView historial;
    private ListaHistorialAdapter adapter;
    private List<Partida> partidas;
    private BBDD_Helper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_partidas);
        getSupportActionBar().hide();

        //Sacamos los elementos de la view
        historial = (RecyclerView)findViewById(R.id.listaHistorial);

        //Inicializamos variables
        helper = new BBDD_Helper(getApplicationContext());
        try {
            partidas = Partida.todasPartidasSimples(helper);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (AppException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //Configuramos la lista
        historial.setHasFixedSize(true);
        historial.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ListaHistorialAdapter(partidas, getString(R.string.label_numeroRondas), getString(R.string.label_jugadorGanador));
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = historial.getChildAdapterPosition(view);
                Partida p = partidas.get(position);
                SingletonMap.getInstancia().put("partida", p);
                SingletonMap.getInstancia().put("volverAtras",true);
                Intent intent = new Intent(HistorialPartidasActivity.this, JugarResultadoPartidaActivity.class);
                startActivity(intent);
            }
        });
        historial.setAdapter(adapter);

    }
}