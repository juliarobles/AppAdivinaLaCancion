package gestion.informacion.appadivinalacancion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import gestion.informacion.appadivinalacancion.util.Modelo.Cancion;
import gestion.informacion.appadivinalacancion.util.Modelo.Jugador;
import gestion.informacion.appadivinalacancion.util.Modelo.Partida;
import gestion.informacion.appadivinalacancion.util.Otros.AppException;
import gestion.informacion.appadivinalacancion.util.Otros.SingletonMap;

public class JugarRondaRespuestaActivity extends AppCompatActivity {

    private TextView titulo;
    private TextView autor;
    private Spinner ganador;
    private ImageView imagenCancion;
    private BBDD_Helper helper;
    private int rondasJugadas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jugar_ronda_respuesta);
        getSupportActionBar().hide();

        //Helper
        helper = new BBDD_Helper(this);

        //Cargamos la lista de jugadores en el Spinner de ganadores

        //Sacamos los elementos del view
        ganador = (Spinner)findViewById(R.id.ganador);
        imagenCancion = (ImageView) findViewById(R.id.imagenCancion);
        titulo = (TextView) findViewById(R.id.textNombreCancion);
        autor = (TextView) findViewById(R.id.text_autorCancion);

        //Sacamos la partida y la cancion de la ronda
        Map info = SingletonMap.getInstancia();
        Partida partida = (Partida) SingletonMap.getInstancia().get("partida");
        rondasJugadas = (int) info.get("rondasJugadas");
        Cancion cancion = partida.getCanciones().get(rondasJugadas-1);

        //Rellenamos los datos de la cancion
        Picasso.get().load(cancion.getImagen().toString()).into(imagenCancion);
        titulo.setText(cancion.getNombre());
        autor.setText(cancion.getAutor());

        //Cargamos la lista de jugadores en el Spinner de ganadores
        List<String> jugadores = new LinkedList<>();
        jugadores.add("Nadie ha acertado");
        for(Jugador j: partida.getJugadores()){
            jugadores.add(j.getNombre());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                jugadores);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ganador.setAdapter(adapter);
        ganador.setSelection(adapter.getPosition("Nadie ha acertado"));

        //Cargamos la imagen de la canción
        imagenCancion = findViewById(R.id.imagenCancion);

     }

    public void respuestaSiguienteRonda(android.view.View v){
        //SIN ACABAR
        //AQUI SE DEBERIAN SUMAR LOS PUNTOS QUE SEA AL JUGADOR QUE SEA O A NADIE

        Map info = SingletonMap.getInstancia();
        long puntos = (long)info.get("puntos");
        Partida partida = (Partida) info.get("partida");
        List<Jugador> jug = partida.getJugadores();

        for(Jugador j :jug){
            System.out.println(j.getPuntos() + "PUNTOS DEL JUGADOR " + j.getNombre());
            if(j.getNombre().equals(ganador.getSelectedItem())){
                try {
                    j.setPuntos(((int)(j.getPuntos()+ (35000 - puntos))), helper);

                } catch (AppException e) {
                    e.printStackTrace();
                }
            }
        }

        if((int)info.get("rondasJugadas") < ((Partida)info.get("partida")).getRondas()){
            Intent intent = new Intent(this, JugarRondaSonarActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, JugarResultadoPartidaActivity.class);
            startActivity(intent);
        }
    }
}