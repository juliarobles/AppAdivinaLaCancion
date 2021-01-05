package gestion.informacion.appadivinalacancion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.List;
import java.util.Map;

import gestion.informacion.appadivinalacancion.util.BBDD.BBDD_Helper;
import gestion.informacion.appadivinalacancion.util.Modelo.Jugador;
import gestion.informacion.appadivinalacancion.util.Modelo.Partida;
import gestion.informacion.appadivinalacancion.util.Otros.SingletonMap;

public class JugarResultadoPartidaActivity extends AppCompatActivity {

    private BBDD_Helper helper;
    private RecyclerView resultados;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jugar_resultado_partida);

        //Inicializamos los elementos de la vista
        resultados =(RecyclerView) findViewById(R.id.resultados);

        //Creamos el helper de la BBDD
        helper = new BBDD_Helper(this);

        //Cargamos los jugadores
        Map info = SingletonMap.getInstancia();
        Partida partida =(Partida) info.get("partida");
        List<Jugador> jugadores = partida.getJugadores();

    }
}