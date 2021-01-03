package gestion.informacion.appadivinalacancion;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Map;
import java.util.Random;

import gestion.informacion.appadivinalacancion.util.Otros.SingletonMap;

public class JugarRondaSonarActivity extends AppCompatActivity {

    private ImageView disco;
    private TextView labelRonda;
    private ObjectAnimator rotar;
    private Button btn_play;
    private Button btn_loTengo;
    private Boolean parado;
    private int rondasJugadas;

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

        //Inicializamos las variables necesarias
        parado = true;

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
    }

    public void respuestaBotonPlay(android.view.View v){
        if (parado){
            //WANY. Aqui deberia seguir sonando la cancion o empezando
            //Tenemos que añadir aqui un temporizador para saber cuanto tiempo se ha reproducido la canción para luego calcular los puntos

            //Cambio el texto del boton y escondo el otro. Sigo rotando.
            btn_play.setText(R.string.pause);
            btn_loTengo.setEnabled(false);
            btn_loTengo.setVisibility(View.INVISIBLE);
            rotar.resume();
            parado = false;
        } else {
            //WANY. Aqui debería pararse la canción
            //Aqui el temporizador tambien deberia pausarse

            //Cambio el texto del boton y muestro el otro. Paro de rotar.
            btn_play.setText(R.string.play);
            btn_loTengo.setEnabled(true);
            btn_loTengo.setVisibility(View.VISIBLE);
            rotar.pause();
            parado = true;
        }
    }

    public void respuestaLoTengo(android.view.View v){
        //SIN ACABAR
        //AQUI VA UN DIALOG QUE ME DA PEREZA AÑADIR, ES TARDE
        //AQUI SE DEBEN CALCULAR LOS POSIBLES PUNTOS RESPECTO AL TEMPORIZADOR
        //AL ACEPTAR EL DIALOG IRIAMOS A LA PANTALLA DE RESULTADO
        Intent intent = new Intent(this, JugarRondaRespuestaActivity.class);
        startActivity(intent);
    }

}