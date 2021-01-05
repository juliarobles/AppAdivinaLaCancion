package gestion.informacion.appadivinalacancion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import gestion.informacion.appadivinalacancion.util.Modelo.Playlist;
import gestion.informacion.appadivinalacancion.util.Otros.ListaPlaylistAdapter;
import gestion.informacion.appadivinalacancion.util.BBDD.BBDD_Helper;
import gestion.informacion.appadivinalacancion.util.Modelo.Partida;
import gestion.informacion.appadivinalacancion.util.Otros.PartidaProvisional;
import gestion.informacion.appadivinalacancion.util.Otros.SingletonMap;
import gestion.informacion.appadivinalacancion.util.Otros.Tupla;

public class EscogerPlaylistActivity extends AppCompatActivity {

    //Crear objetos de la pantalla
    private TextView urlPlaylist;
    private TextView numRondas;
    private RecyclerView listaPlaylist;
    private List<Tupla> playlists;
    private BBDD_Helper helper;

    public static String getParamsString(Map<String, String> params)
            throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            result.append("&");
        }

        String resultString = result.toString();
        return resultString.length() > 0
                ? resultString.substring(0, resultString.length() - 1)
                : resultString;
    }

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

        //Configuramos la recyclerView
        listaPlaylist.setHasFixedSize(true);
        listaPlaylist.setLayoutManager(new LinearLayoutManager(this));
        ponerEjemplos();
        ListaPlaylistAdapter adapter = new ListaPlaylistAdapter(playlists);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tupla t = playlists.get(listaPlaylist.getChildAdapterPosition(view));
                urlPlaylist.setText(t.getX2());
                Toast.makeText(getApplicationContext(), getString(R.string.mensaje_playlistCambiada) + " " + t.getX1(), Toast.LENGTH_SHORT).show();
            }
        });
        listaPlaylist.setAdapter(adapter);

        //activarSpotify();
    }

    private void activarSpotify(){
        //Prueba conexion URL
        //https://www.baeldung.com/java-http-request

        /*
        Datos Spotify
        Client ID: c4efe3f6719249d7b465386ced876161
        Client Secret: c4da7a4469594e8ebd5a29148458efa4
         */
       /* AsyncTask.execute(new Runnable(){

            @Override
            public void run() {
                try {

                    System.out.println("Que está pasando");
                    URL url = new URL("https://accounts.spotify.com/api/token");
                    HttpURLConnection con;

                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");

                    //Datos header
                    con.setRequestProperty("Authorization", "Basic");
                    //Parametros
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    String jsonInputString = "{grant_type:c4efe3f6719249d7b465386ced876161c4da7a4469594e8ebd5a29148458efa4}";

                    OutputStream os = con.getOutputStream();
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);


                    int code = con.getResponseCode();
                    //nombrePlaylist.setText(con.getResponseCode());
                    System.out.println(code + " el bichooooooooooooo");

                    InputStream stream = con.getInputStream();
                    System.out.println(stream.toString());
                    BufferedReader br = new BufferedReader(new InputStreamReader(stream, "utf-8"));
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while((responseLine = br.readLine())!=null){
                        response.append(responseLine.trim());
                        System.out.println("Leyendo");
                    }
                    System.out.println("Respuesta:");

                    System.out.println(response.toString());


                    con.disconnect();

                } catch (MalformedURLException | ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } );*/
    }

    private void ponerEjemplos() {
        playlists = new ArrayList<>();
        playlists.add(new Tupla("El Top 50 Global", "https://open.spotify.com/playlist/37i9dQZEVXbMDoHDwVN2tF"));
        playlists.add(new Tupla("El Top 50 de España", "https://open.spotify.com/playlist/37i9dQZEVXbNFJfN1Vw8d9"));
        playlists.add(new Tupla("Los 50 más virales global", "https://open.spotify.com/playlist/37i9dQZEVXbLiRSasKsNU9"));
        playlists.add(new Tupla("Los 50 más virales de España", "https://open.spotify.com/playlist/37i9dQZEVXbMfVLvbaC3bj"));
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

    public void cambiarPlaylistMasUsadas(android.view.View v){
        //playlists =
    }

    public void cambiarPlaylistRecientes(android.view.View v){
        //playlists =
    }

    public void cambiarPlaylistEjemplos(android.view.View v){
        ponerEjemplos();
    }


}