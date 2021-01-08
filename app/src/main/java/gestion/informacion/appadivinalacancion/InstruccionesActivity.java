package gestion.informacion.appadivinalacancion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import gestion.informacion.appadivinalacancion.R;

public class InstruccionesActivity extends AppCompatActivity {

    private TextView instruccionesTitulo;
    private TabLayout tab_instrucciones;
    private TabItem tab_item_configuracion, tab_item_juego;
    private ViewPager2 pager_instrucciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instrucciones);
        instruccionesTitulo = (TextView) findViewById(R.id.instrucciontesTitulo);
        instruccionesTitulo.setText(R.string.title_instrucciones);
        getSupportActionBar().hide();

    }
}