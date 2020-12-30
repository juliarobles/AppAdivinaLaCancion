package gestion.informacion.appadivinalacancion;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class EscogerPlaylistActivity extends AppCompatActivity {


        //Crear objetos de la pantalla
    private TextView nombrePlaylist;
    private ImageView imagenPlaylist;
    private String urlImagen;

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

        //Elementos vista
        nombrePlaylist = (TextView) findViewById(R.id.nombrePlaylist);
        imagenPlaylist = (ImageView) findViewById(R.id.imagenPlaylist);
        buscarCancion();



    }

    private void buscarCancion(){
        AsyncTask.execute(new Runnable() {

            @Override
            public void run() {
                try {
                    System.out.println("Buscando una canción");
                    URL url = new URL("https://api.spotify.com/v1/tracks/2TpxZ7JUBn3uw46aR7qd6V");
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    //Hay que ir actualizando el token cada cierto tiempo (?)
                    String token = "BQCWOr9sVLqqpXPts2i2Cypcdesg_N6an7bkLb3uDZU5CKglvmnGtrUx1b2nUA07_2lpbFfVOy7sgnbtOzc";
                    con.setRequestProperty("Authorization", "Bearer " + token);

                    //Si esto funciona hacer una función para leer los bytes que nos devuelve la URL
                    Reader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                    StringBuilder sb = new StringBuilder();
                    for(int c ;(c = in.read())>=0; ){
                        sb.append((char) c);
                    }
                    String response = sb.toString();
                    System.out.println(response);
                    JSONObject myResponse = new JSONObject(response.toString());
                    System.out.println(myResponse.get("id"));
                    String urlAlbum = myResponse.getJSONObject("album").getJSONArray("images").getJSONObject(1).getString("url");
                    System.out.println(" Este es el resultado de hacer un get album + images");

                    int code = con.getResponseCode();
                    //nombrePlaylist.setText(con.getResponseCode());
                    System.out.println(code + " el bichooooooooooooo buscando una canción");




                    //probando a setear la imagen
                    urlImagen = urlAlbum;
                    System.out.println("Ya he guardado la imagen");


                } catch (MalformedURLException | ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        try {

            System.out.println("Cargando la imagen " + urlImagen);
            URL url = new URL(urlImagen);
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            imagenPlaylist.setImageBitmap(bmp);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void generarToken(){
            //Prueba conexion URL
            //https://www.baeldung.com/java-http-request

            /*
            Datos Spotify
            Client ID: c4efe3f6719249d7b465386ced876161
            Client Secret: c4da7a4469594e8ebd5a29148458efa4
            "access_token":"BQCWOr9sVLqqpXPts2i2Cypcdesg_N6an7bkLb3uDZU5CKglvmnGtrUx1b2nUA07_2lpbFfVOy7sgnbtOzc"
            "token_type":"Bearer"
             */
            AsyncTask.execute(new Runnable(){

                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void run() {
                    try {

                        System.out.println("Que está pasando");
                        URL url = new URL("https://accounts.spotify.com/api/token");

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
                        System.out.println(response);
                        JSONObject myResponse = new JSONObject(response.toString());
                        int code = con.getResponseCode();
                        //nombrePlaylist.setText(con.getResponseCode());
                        System.out.println(code + " el bichooooooooooooo");
                        con.disconnect();

                    } catch (MalformedURLException | ProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } );
        }


}