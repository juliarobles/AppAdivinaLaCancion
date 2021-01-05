package gestion.informacion.appadivinalacancion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import gestion.informacion.appadivinalacancion.util.BBDD.BBDD_Helper;
import gestion.informacion.appadivinalacancion.util.Modelo.Jugador;
import gestion.informacion.appadivinalacancion.util.Modelo.Partida;
import gestion.informacion.appadivinalacancion.util.Otros.PuntosAdapter;
import gestion.informacion.appadivinalacancion.util.Otros.SingletonMap;

public class JugarResultadoPartidaActivity extends AppCompatActivity {

    private BBDD_Helper helper;
    private RecyclerView recyclerResultados;
    private TextView textResultados;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jugar_resultado_partida);

        //Inicializamos los elementos de la vista
        recyclerResultados =(RecyclerView) findViewById(R.id.resultados);
        //Hay que meterle el mensaje
        textResultados = (TextView) findViewById(R.id.textResultados);
        //Creamos el helper de la BBDD
        helper = new BBDD_Helper(this);

        //Cargamos los jugadores
        Map info = SingletonMap.getInstancia();
        Partida partida =(Partida) info.get("partida");
        List<Jugador> jugadores = partida.getJugadoresSortByPuntos(partida, helper);
        System.out.println(jugadores.size() + " NUMERO DE JUGADORES");
        PuntosAdapter adapter = new PuntosAdapter(jugadores);
        recyclerResultados.setHasFixedSize(true);
        recyclerResultados.setLayoutManager(new LinearLayoutManager((this)));
        recyclerResultados.setAdapter(adapter);

        //Establecemos los textos
        //Cambiar esto
        textResultados.setText("Resultados");
    }
}