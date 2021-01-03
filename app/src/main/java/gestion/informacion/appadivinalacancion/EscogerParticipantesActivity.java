package gestion.informacion.appadivinalacancion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import gestion.informacion.appadivinalacancion.util.Modelo.JugadorProvisional;
import gestion.informacion.appadivinalacancion.util.Modelo.Partida;
import gestion.informacion.appadivinalacancion.util.Otros.ListaJugadoresAdapter;
import gestion.informacion.appadivinalacancion.util.Otros.SingletonMap;

public class EscogerParticipantesActivity extends AppCompatActivity {

    private TextView nombreParticipante;
    private RecyclerView listaJugadores;
    private RecyclerView.Adapter adapter;
    private ImageView avatar;
    private List<JugadorProvisional> jugadores;
    private int numParticipantes;
    private List<Integer> avatars;
    private Partida partida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escoger_participantes);
        getSupportActionBar().hide();

        //Elementos de la vista
        nombreParticipante = (TextView) findViewById(R.id.nombreParticipante);
        listaJugadores = (RecyclerView) findViewById(R.id.listaJugadores);
        avatar = (ImageView) findViewById(R.id.avatar);

        //Inicializo variables
        jugadores = new ArrayList<>();
        numParticipantes = 0;
        inicializarAvatars();
        partida = (Partida) SingletonMap.getInstancia().get("partida");

        //Pongo un avatar aleatorio
        Collections.shuffle(avatars);
        avatar.setImageResource(avatars.get(numParticipantes));

        //Configuramos la lista
        listaJugadores.setHasFixedSize(true);
        listaJugadores.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ListaJugadoresAdapter(jugadores);
        listaJugadores.setAdapter(adapter);

    }

    public void respuestaAddParticipante(android.view.View v){
        if(numParticipantes < 7){
            //Sacamos el nombre
            String nombre = nombreParticipante.getText().toString();
            System.out.println("Nombre: " + nombre);
            if(nombre == null || nombre.trim().equals("")){
                Toast.makeText(getApplicationContext(), R.string.falloNombreVacio, Toast.LENGTH_SHORT).show();
            } else {
                //Creo jugador provisional y lo añado a la lista
                jugadores.add(new JugadorProvisional(nombre, partida, avatars.get(numParticipantes)));

                //Siguiente avatar
                numParticipantes++;
                avatar.setImageResource(avatars.get(numParticipantes));

                //Elimino lo que hay en el textView
                nombreParticipante.setText("");

                //Actualizo la lista
                adapter.notifyDataSetChanged();
            }
        } else {
            Toast.makeText(getApplicationContext(), R.string.falloMaxJugadores, Toast.LENGTH_SHORT).show();
        }


    }

    private void inicializarAvatars() {
        avatars = new ArrayList<>();
        avatars.add(R.drawable.ic_avatar_zombi);
        avatars.add(R.drawable.ic_avatar_astronauta);
        avatars.add(R.drawable.ic_avatar_enfermera);
        avatars.add(R.drawable.ic_avatar_hada);
        avatars.add(R.drawable.ic_avatar_mago);
        avatars.add(R.drawable.ic_avatar_momia);
        avatars.add(R.drawable.ic_avatar_monstruo);
        avatars.add(R.drawable.ic_avatar_payaso);
        avatars.add(R.drawable.ic_avatar_unicornio);
        avatars.add(R.drawable.ic_avatar_robot);
    }
}