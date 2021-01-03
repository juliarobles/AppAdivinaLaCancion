package gestion.informacion.appadivinalacancion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btn_jugar;
    private Button btn_instrucciones;
    private Button btn_historial;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        getSupportActionBar().hide();
        //btn_jugar = (Button) findViewById(R.id.btn_jugar);
        //btn_instrucciones = (Button) findViewById(R.id.btn_instrucciones);
        //btn_historial = (Button) findViewById(R.id.btn_historial);
    }

    public void respuestaJugar(android.view.View v){
        Intent intent = new Intent(this, EscogerPlaylistActivity.class);
        startActivity(intent);
    }


}
