package gestion.informacion.appadivinalacancion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class EscogerPlaylistActivity extends AppCompatActivity {


        //Crear objetos de la pantalla
    private TextView nombrePlaylist;

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
        nombrePlaylist = (TextView)findViewById(R.id.nombrePlaylist);

        //Prueba conexion URL
        //https://www.baeldung.com/java-http-request

        /*
        Datos Spotify
        Client ID: c4efe3f6719249d7b465386ced876161
        Client Secret: c4da7a4469594e8ebd5a29148458efa4
         */
        AsyncTask.execute(new Runnable(){

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
        } );


    }


}