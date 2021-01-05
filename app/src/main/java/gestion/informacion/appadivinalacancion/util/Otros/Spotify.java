package gestion.informacion.appadivinalacancion.util.Otros;

import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import gestion.informacion.appadivinalacancion.util.BBDD.BBDD_Helper;
import gestion.informacion.appadivinalacancion.util.Modelo.Cancion;

public  class Spotify {

    //Datos cuenta Spotify
    private static final String ClientID = "c4efe3f6719249d7b465386ced876161";
    private static final String ClientSecret ="c4da7a4469594e8ebd5a29148458efa4";

    private String token;

    private BBDD_Helper helper;
    public Spotify (BBDD_Helper helper){
        this.helper = helper;
        Object[] obj =  {"token"};
        SpotifyTask task = new SpotifyTask();

        Object t =task.execute(obj);
        if(t != null && t instanceof String){
            token = (String) t;
        }
        System.out.println("Token actualizado " + this.token );
    }

    public List<Cancion> getCancionesFromPlaylist(String id, int numero){
        SpotifyTask task = new SpotifyTask();

        String url = 	"https://api.spotify.com/v1/playlists/"+id+"/tracks";

        Object[] obj  = {"busqueda", "GET", url};
        Object res = null;
        try {
            res = task.execute(obj).get();
            if(res != null && res instanceof JSONObject){
                JSONObject respuesta = (JSONObject)res;
                System.out.println("BUSQUEDA FINALIZADA");
                System.out.println("LISTA CANCIONES: " );
                JSONObject aux;
                List<Integer> usados = new LinkedList<>();
                List<Cancion> listaCanciones = new LinkedList<>();
                int cont = 0;
                Random r = new Random();
                while(cont < numero){
                    int random = r.nextInt(respuesta.getJSONArray("items").length());
                    if(!usados.contains(random)) {
                        aux = respuesta.getJSONArray("items").getJSONObject(cont).getJSONObject("track");
                        System.out.println("Canción número " + cont + ": " + aux);
                        Cancion c = new Cancion(aux.getString("name"),
                                new URL("https://api.spotify.com/v1/tracks/" + aux.getString("id")),
                                stringArtistas(aux.getJSONArray("artists")), null, this.helper);
                        cont++;
                        usados.add(random);
                    }
                }
            }else{
                System.out.println("ERROR AL EJECUTAR LA OPERACIÓN");
                System.out.println(res);
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    private String stringArtistas(JSONArray artists) {
        JSONObject artista;
        StringBuilder sb =  new StringBuilder();

        for(int cont = 0; cont < artists.length();cont++){
            try {
                artista = artists.getJSONObject(cont);
                sb.append(artista.getString("name") );
                if(cont != artists.length()-1){
                    sb.append(", ");
                }else{
                    sb.append(".");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return sb.toString();
    }

    public class SpotifyTask extends AsyncTask{

        private JSONObject crearRequest(Object [] obj){
            //En principio deberían de venir en este orden
            String metodo = (String) obj[1];
            String enlace = (String) obj[2];

            try {
                URL url = new URL(enlace);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod(metodo);

                con.setRequestProperty("Authorization", "Bearer " + token);
                Reader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                StringBuilder sb = new StringBuilder();
                for(int c ;(c = in.read())>=0; ){
                    sb.append((char) c);
                }
                String response = sb.toString();

                JSONObject myResponse = new JSONObject(response.toString());
                System.out.println(con.getResponseCode());
                System.out.println("Respuesta: " + myResponse);
                return myResponse;
            } catch (MalformedURLException | JSONException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        @RequiresApi(api = Build.VERSION_CODES.O)
        private String actualizarToken(Object[] objects){

            URL url = null;
            try {

                 url = new URL("https://accounts.spotify.com/api/token");

                String postData = "grant_type=client_credentials";
                byte [] postDataBytes = postData.toString().getBytes("UTF-8");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");

                //Datos header
                String clientCredEncode = Base64.getEncoder().encodeToString(("c4efe3f6719249d7b465386ced876161:c4da7a4469594e8ebd5a29148458efa4").getBytes());
                con.setRequestProperty("Authorization",String.format("Basic %s", clientCredEncode));
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                //Parametros
                con.setDoInput(true);
                con.setDoOutput(true);
                con.getOutputStream().write(postDataBytes);
                //Input Stream
                Reader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                StringBuilder sb = new StringBuilder();
                for(int c ;(c = in.read())>=0; ){
                    sb.append((char) c);
                }
                String response = sb.toString();

                JSONObject myResponse = new JSONObject(response.toString());
                token =  myResponse.getString("access_token");
                int code = con.getResponseCode();
                //nombrePlaylist.setText(con.getResponseCode());
                System.out.println(code + " el bichooooooooooooo");
                con.disconnect();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Object doInBackground(Object[] objects) {
            Object obj = null;
            if(objects[0] == "token"){
                obj = actualizarToken(objects);
            }else if(objects[0]== "busqueda"){
                obj = crearRequest(objects);
            }

            return obj;
        }
    }
}
