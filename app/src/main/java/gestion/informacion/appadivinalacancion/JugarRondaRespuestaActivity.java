package gestion.informacion.appadivinalacancion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import gestion.informacion.appadivinalacancion.util.Modelo.Jugador;
import gestion.informacion.appadivinalacancion.util.Modelo.Partida;
import gestion.informacion.appadivinalacancion.util.Otros.SingletonMap;

public class JugarRondaRespuestaActivity extends AppCompatActivity {

    private Spinner ganador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jugar_ronda_respuesta);
        getSupportActionBar().hide();
        ganador = (Spinner)findViewById(R.id.ganador);
        Partida partida = (Partida) SingletonMap.getInstancia().get("partida");

        List<String> jugadores = new LinkedList<>();
        for(Jugador j: partida.getJugadores()){
            jugadores.add(j.getNombre());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                jugadores);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ganador.setAdapter(adapter);
     }

    public void respuestaSiguienteRonda(android.view.View v){
        //SIN ACABAR
        //AQUI SE DEBERIAN SUMAR LOS PUNTOS QUE SEA AL JUGADOR QUE SEA O A NADIE

        Map info = SingletonMap.getInstancia();

        if((int)info.get("rondasJugadas") < ((Partida)info.get("partida")).getRondas()){
            Intent intent = new Intent(this, JugarRondaSonarActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, JugarResultadoPartidaActivity.class);
            startActivity(intent);
        }
    }
}