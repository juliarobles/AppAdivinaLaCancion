package gestion.informacion.appadivinalacancion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import gestion.informacion.appadivinalacancion.util.BBDD.BBDD_Helper;
import gestion.informacion.appadivinalacancion.util.Modelo.Cancion;
import gestion.informacion.appadivinalacancion.util.Modelo.Playlist;
import gestion.informacion.appadivinalacancion.util.Otros.JugadorProvisional;
import gestion.informacion.appadivinalacancion.util.Modelo.Partida;
import gestion.informacion.appadivinalacancion.util.Adaptadores.ListaJugadoresAdapter;
import gestion.informacion.appadivinalacancion.util.Otros.PartidaProvisional;
import gestion.informacion.appadivinalacancion.util.Otros.SingletonMap;
import gestion.informacion.appadivinalacancion.util.Otros.Spotify;

public class EscogerParticipantesActivity extends AppCompatActivity {

    private TextView nombreParticipante;
    private RecyclerView listaJugadores;
    private ListaJugadoresAdapter adapter;
    private ImageView avatar;
    private List<JugadorProvisional> jugadores;
    private int numParticipantes;
    private List<Integer> avatars;
    private PartidaProvisional partidaProvisional;
    private BBDD_Helper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escoger_participantes);
        getSupportActionBar().hide();

        //Elementos de la vista
        nombreParticipante = (TextView) findViewById(R.id.nombreParticipante);
        listaJugadores = (RecyclerView) findViewById(R.id.listaJugadores);
        avatar = (ImageView) findViewById(R.id.avatar);

        //Creamos el helper de la BBDD
        helper = new BBDD_Helper(getApplicationContext());

        //Inicializo variables
        jugadores = new ArrayList<>();
        numParticipantes = 0;
        inicializarAvatars();
        partidaProvisional = (PartidaProvisional) SingletonMap.getInstancia().get("partida");

        //Pongo un avatar aleatorio
        Collections.shuffle(avatars);
        avatar.setImageResource(avatars.get(numParticipantes));

        //Configuramos la lista
        listaJugadores.setHasFixedSize(true);
        listaJugadores.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ListaJugadoresAdapter(jugadores);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = listaJugadores.getChildAdapterPosition(view);
                JugadorProvisional jugador = jugadores.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(EscogerParticipantesActivity.this);
                builder.setMessage(getString(R.string.avisoSeguroEliminarJugador) + " " + jugador.getNombre() + getString(R.string.finpregunta))
                        .setTitle(R.string.avisoEliminarJugador);
                builder.setPositiveButton(R.string.eliminar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int color = jugador.getColor();
                        //Elimino jugador
                        jugadores.remove(jugador);
                        numParticipantes--;
                        //Pongo el avatar que tenía el último de la lista
                        avatars.remove(new Integer(color));
                        avatars.add(color);
                        //Actualizo el recyclerView
                        //GRACIAS: https://stackoverflow.com/questions/31367599/how-to-update-recyclerview-adapter-data
                        adapter.notifyDataSetChanged();
                        listaJugadores.removeViewAt(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(position, jugadores.size());
                    }
                });
                builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                builder.create().show();
            }
        });
        listaJugadores.setAdapter(adapter);

    }

    public void respuestaAddParticipante(android.view.View v){
        if(numParticipantes < 7){
            //Sacamos el nombre
            String nombre = nombreParticipante.getText().toString();

            if(nombre == null || nombre.trim().equals("")){
                Toast.makeText(getApplicationContext(), R.string.falloNombreVacio, Toast.LENGTH_SHORT).show();
            } else {
                //Creo jugador provisional y lo añado a la lista
                jugadores.add(new JugadorProvisional(nombre, avatars.get(numParticipantes)));

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

    public void respuestaEmpezarJuego(android.view.View v){
        try {
            if(numParticipantes >= 1){
                Map map = SingletonMap.getInstancia();
                Spotify sp = new Spotify(this.helper);
                //Creo la partida de verdad
                String [] partes = partidaProvisional.getPlaylist().toString().split("/|\\?");
                String id;

                if(!partes[3].equals("v1")){
                     id = partes[4];

                }else{
                    id = partes[5];

                }
                Playlist playlist = sp.getPlaylistFromUrl(id);


                Partida partidaDefinitiva = new Partida(partidaProvisional.getFecha(), partidaProvisional.getRondas(), playlist, helper); //SUSTITUYE null POR PLAYLIST

                //Establezco los jugadores como definitivos (osea los guardo en la bd)
                partidaDefinitiva.setJugadores(jugadores, helper);

                //Guardo la partida en nuestra información
                map.replace("partida", partidaDefinitiva);

                //Esta variable nos permitirá tener saber cuantas rondas hemos jugado
                map.put("rondasJugadas", 0);

                //Creamos la lista de canciones que va a sonar
                List<Cancion> canciones = sp.getCancionesFromPlaylist(id, partidaProvisional.getRondas());
                if(canciones.size() <=0){
                    Toast.makeText(getApplicationContext(), R.string.playlist_no_valida, Toast.LENGTH_SHORT).show();
                }else{
                    partidaDefinitiva.setCanciones(canciones,helper);
                    partidaDefinitiva.setRondas(canciones.size(), helper);
                    map.replace("partida", partidaDefinitiva);

                    //Comienzo el juego

                    Intent intent = new Intent(this, JugarRondaSonarActivity.class);
                    startActivity(intent);
                }

            } else {
                Toast.makeText(getApplicationContext(), R.string.falloNoJugadores, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), R.string.falloCrearPartida, Toast.LENGTH_SHORT).show();
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