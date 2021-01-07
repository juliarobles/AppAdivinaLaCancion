package gestion.informacion.appadivinalacancion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import gestion.informacion.appadivinalacancion.util.BBDD.BBDD_Helper;
import gestion.informacion.appadivinalacancion.util.Modelo.Jugador;
import gestion.informacion.appadivinalacancion.util.Modelo.Partida;
import gestion.informacion.appadivinalacancion.util.Adaptadores.PuntosAdapter;
import gestion.informacion.appadivinalacancion.util.Otros.SingletonMap;

public class JugarResultadoPartidaActivity extends AppCompatActivity {

    private BBDD_Helper helper;
    private RecyclerView recyclerResultados;
    private TextView textResultados;
    private List<Jugador> jugadores;
    private boolean volverAtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jugar_resultado_partida);
        getSupportActionBar().hide();

        //Inicializamos los elementos de la vista
        recyclerResultados =(RecyclerView) findViewById(R.id.resultados);

        //Creamos el helper de la BBDD
        helper = new BBDD_Helper(this);
        volverAtras = false;

        //Cargamos los jugadores
        Map info = SingletonMap.getInstancia();
        Partida partida =(Partida) info.get("partida");
        jugadores = partida.getJugadoresSortByPuntos(partida, helper);
        try {
            partida.setGanador(jugadores.get(0), helper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(info.containsKey("volverAtras")){
            if((boolean)info.get("volverAtras")){
                volverAtras = true;
            }
            info.remove("volverAtras");
        }

        info.remove("partida");
        info.remove("rondasJugadas");
        PuntosAdapter adapter = new PuntosAdapter(jugadores, getString(R.string.label_puntos), getString(R.string.label_acertadas));
        recyclerResultados.setHasFixedSize(true);
        recyclerResultados.setLayoutManager(new LinearLayoutManager((this)));
        recyclerResultados.setAdapter(adapter);
    }

    public void respuestaAcabarPartida(android.view.View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if(volverAtras){
            super.onBackPressed();
        }
    }
}