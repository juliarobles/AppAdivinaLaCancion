package gestion.informacion.appadivinalacancion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gestion.informacion.appadivinalacancion.util.Adaptadores.ListaPlaylistAdapter;
import gestion.informacion.appadivinalacancion.util.BBDD.BBDD_Helper;
import gestion.informacion.appadivinalacancion.util.Modelo.Partida;
import gestion.informacion.appadivinalacancion.util.Otros.PartidaProvisional;
import gestion.informacion.appadivinalacancion.util.Otros.SingletonMap;
import gestion.informacion.appadivinalacancion.util.Otros.PlaylistProvisional;

public class EscogerPlaylistActivity extends AppCompatActivity {

    //Crear objetos de la pantalla
    private TextView urlPlaylist;
    private TextView numRondas;
    private RecyclerView listaPlaylist;
    private List<PlaylistProvisional> playlists;
    private BBDD_Helper helper;
    private ListaPlaylistAdapter adapter;
    private TabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escoger_playlist);
        getSupportActionBar().hide();

        //Creamos el helper de la BBDD
        helper = new BBDD_Helper(getApplicationContext());

        //Sacamos los elementos de la vista
        urlPlaylist = (TextView)findViewById(R.id.urlPlaylist);
        numRondas = (TextView)findViewById(R.id.numRondas);
        listaPlaylist = (RecyclerView)findViewById(R.id.listaPlaylist);
        tabs = (TabLayout) findViewById(R.id.tabs_elegirPlaylist);

        //Configuramos la recyclerView
        listaPlaylist.setHasFixedSize(true);
        listaPlaylist.setLayoutManager(new LinearLayoutManager(this));
        ponerEjemplos();
        adapter = new ListaPlaylistAdapter(playlists);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaylistProvisional t = playlists.get(listaPlaylist.getChildAdapterPosition(view));
                urlPlaylist.setText(t.getUrlPl());
                Toast.makeText(getApplicationContext(), getString(R.string.mensaje_playlistCambiada) + " " + t.getTitulo(), Toast.LENGTH_SHORT).show();
            }
        });
        listaPlaylist.setAdapter(adapter);

        //Configuramos el tabs
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                if(pos == 1) { //MAS USADAS
                    try {
                        playlists.clear();
                        playlists.addAll(Partida.playlistMasUsadas(helper));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                } else if (pos == 2){
                    try {
                        playlists.clear();
                        playlists.addAll(Partida.playlistRecientes(helper));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                } else { //EJEMPLOS
                    ponerEjemplos();
                }
                adapter.notifyDataSetChanged();
                listaPlaylist.removeAllViews();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void ponerEjemplos() {
        if(playlists == null){
            playlists = new ArrayList<>();
        } else {
            playlists.clear();
        }
        playlists.add(new PlaylistProvisional("Global Top 50", "https://open.spotify.com/playlist/37i9dQZEVXbMDoHDwVN2tF", "https://charts-images.scdn.co/assets/locale_en/regional/daily/region_global_large.jpg", "spotifycharts"));
        playlists.add(new PlaylistProvisional("Spain Top 50", "https://open.spotify.com/playlist/37i9dQZEVXbNFJfN1Vw8d9","https://charts-images.scdn.co/assets/locale_en/regional/daily/region_es_large.jpg", "spotifycharts"));
        playlists.add(new PlaylistProvisional("Global Viral 50", "https://open.spotify.com/playlist/37i9dQZEVXbLiRSasKsNU9", "https://charts-images.scdn.co/assets/locale_en/viral/daily/region_global_large.jpg", "spotifycharts"));
        playlists.add(new PlaylistProvisional("Spain Viral 50", "https://open.spotify.com/playlist/37i9dQZEVXbMfVLvbaC3bj", "https://charts-images.scdn.co/assets/locale_en/viral/daily/region_es_large.jpg", "spotifycharts"));
    }

    public void respuestaEscogerPlaylist(android.view.View v){
        try {
            //Sacamos los valores añadidos por el usuario
            String valorRondas = numRondas.getText().toString();
            if(valorRondas == null || valorRondas.equals("")) {
                Toast.makeText(getApplicationContext(), R.string.falloMenorIgualCero, Toast.LENGTH_LONG).show();
            } else {
                int rondas = Integer.parseInt(valorRondas);
                if(rondas <= 0 || rondas > 100){
                    Toast.makeText(getApplicationContext(), R.string.falloMenorIgualCero, Toast.LENGTH_LONG).show();
                } else {
                    String valorURL = urlPlaylist.getText().toString();
                    URL url = new URL(valorURL);

                    //Creamos la partida
                    PartidaProvisional partida = new PartidaProvisional(new Date(), rondas, url);
                    SingletonMap.getInstancia().put("partida", partida);

                    //Pasamos a la siguiente pantalla
                    Intent intent = new Intent(this, EscogerParticipantesActivity.class);
                    startActivity(intent);
                }
            }
        } catch (MalformedURLException e) {
            Toast.makeText(getApplicationContext(), R.string.falloURL, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.falloCrearPartida, Toast.LENGTH_LONG).show();
        }
    }

    public void cambiarPlaylist(android.view.View v){

    }

    public void cambiarPlaylistRecientes(android.view.View v){
        //playlists =
    }

    public void cambiarPlaylistEjemplos(android.view.View v){
        ;
    }


}