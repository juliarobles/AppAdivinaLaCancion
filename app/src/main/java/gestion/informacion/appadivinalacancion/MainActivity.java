package gestion.informacion.appadivinalacancion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        getSupportActionBar().hide();
    }

    public void respuestaJugar(android.view.View v){
        Intent intent = new Intent(this, EscogerPlaylistActivity.class);
        startActivity(intent);
    }

    public void respuestaHistorial(android.view.View v){
        Intent intent = new Intent(this, HistorialPartidasActivity.class);
        startActivity(intent);
    }

    public void respuestaInstrucciones(android.view.View v){
        Intent intent = new Intent(this, InstruccionesActivity.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {

    }

}
