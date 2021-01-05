package gestion.informacion.appadivinalacancion;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.Map;
import java.util.Random;

import gestion.informacion.appadivinalacancion.util.BBDD.BBDD_Helper;
import gestion.informacion.appadivinalacancion.util.Modelo.Cancion;
import gestion.informacion.appadivinalacancion.util.Modelo.Partida;
import gestion.informacion.appadivinalacancion.util.Otros.SingletonMap;
import gestion.informacion.appadivinalacancion.util.Otros.Spotify;

public class JugarRondaSonarActivity extends AppCompatActivity {

    private ImageView disco;
    private TextView labelRonda;
    private TextView labelCronometro;
    private ObjectAnimator rotar;
    private Button btn_play;
    private Button btn_loTengo;
    private Boolean parado;
    private int rondasJugadas;
    private Chronometer cronometro;
    private long pauseOffset;
    private Cancion cancionActual;
    private Spotify sp;
    private BBDD_Helper helper;
    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jugar_ronda_sonar);
        getSupportActionBar().hide();

        //Sacamos los elementos de la vista
        disco = (ImageView) findViewById(R.id.disco);
        btn_play = (Button) findViewById(R.id.btn_play);
        btn_loTengo = (Button) findViewById(R.id.btn_loTengo);
        labelRonda = (TextView) findViewById(R.id.textoRonda);
        cronometro = (Chronometer) findViewById(R.id.cronometro);

        //Inicializamos las variables necesarias
        parado = true;
        pauseOffset = 0;

        //Pongo el numero de ronda y lo cambio en nuestra información
        Map info = SingletonMap.getInstancia();
        rondasJugadas = ((int) info.get("rondasJugadas")) + 1;
        info.replace("rondasJugadas", rondasJugadas);
        labelRonda.setText(getString(R.string.labelRonda) + rondasJugadas);

        //Hacemos la animación del disco pero lo dejo parado por ahora
        //GRACIAS: https://stackoverflow.com/questions/58037754/how-to-pause-and-resume-rotate-animation-android
        rotar = ObjectAnimator.ofFloat(disco, View.ROTATION, 0f, 360f)
                .setDuration(10000);
        rotar.setRepeatCount(Animation.INFINITE);
        rotar.setInterpolator(new LinearInterpolator());
        rotar.start();
        rotar.pause();

        //Inicializamos el cronometro
        cronometro.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if((SystemClock.elapsedRealtime() - cronometro.getBase()) >= 30000){
                    cronometro.stop();
                    btn_loTengo.setEnabled(true);
                    rotar.pause();
                    parado = true;
                    btn_play.setEnabled(false);
                    btn_play.setText(R.string.avisoFinTiempo);
                }
            }
        });
        helper = new BBDD_Helper(getApplicationContext());
        sp = new Spotify(helper);
        cancionActual = ((Partida)SingletonMap.getInstancia().get("partida")).getCanciones().get(this.rondasJugadas-1);
        System.out.println(cancionActual.getUrl() + " NOMBRE DE LA CANCION");
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mediaPlayer.setDataSource(sp.getPreviewCancion(cancionActual.getUrl().toString()));
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void respuestaBotonPlay(android.view.View v){
        if (parado){

            mediaPlayer.start();
            //El temporizador sigue corriendo
            cronometro.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            cronometro.start();
            //Cambio el texto del boton y escondo el otro. Sigo rotando.
            btn_play.setText(R.string.pause);
            btn_loTengo.setEnabled(false);
            rotar.resume();
            parado = false;
        } else {
            System.out.println("TIEMPO TRANSCURRIDO: " +(SystemClock.elapsedRealtime() -  cronometro.getBase()));

            mediaPlayer.pause();
            //Aqui el temporizador se pausa
            cronometro.stop();
            pauseOffset = SystemClock.elapsedRealtime() - cronometro.getBase();
            //Cambio el texto del boton y muestro el otro. Paro de rotar.
            btn_play.setText(R.string.play);
            btn_loTengo.setEnabled(true);
            rotar.pause();
            parado = true;
        }
    }

    public void respuestaLoTengo(android.view.View v){
        //SIN ACABAR
        //AQUI VA UN DIALOG QUE ME DA PEREZA AÑADIR, ES TARDE
        //AQUI SE DEBEN CALCULAR LOS POSIBLES PUNTOS RESPECTO AL TEMPORIZADOR
        //AL ACEPTAR EL DIALOG IRIAMOS A LA PANTALLA DE RESULTADO
        SingletonMap.getInstancia().put("puntos", SystemClock.elapsedRealtime() - cronometro.getBase());
        Intent intent = new Intent(this, JugarRondaRespuestaActivity.class);
        startActivity(intent);
    }

}