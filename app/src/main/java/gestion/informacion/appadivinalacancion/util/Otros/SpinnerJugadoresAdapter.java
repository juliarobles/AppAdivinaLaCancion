package gestion.informacion.appadivinalacancion.util.Otros;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import gestion.informacion.appadivinalacancion.R;
import gestion.informacion.appadivinalacancion.util.Modelo.Jugador;

public class SpinnerJugadoresAdapter extends ArrayAdapter<Jugador> {
    private String texto;

    public SpinnerJugadoresAdapter(Context context, List<Jugador> lista, String texto){
        super(context, 0, lista);
        this.texto = texto;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_selectjugadores, parent, false
            );
        }

        ImageView imagen = (ImageView) convertView.findViewById(R.id.select_item_avatarJugador);
        TextView nombre = (TextView) convertView.findViewById(R.id.select_item_nombreJugador);
        TextView puntos = (TextView) convertView.findViewById(R.id.select_item_puntosJugador);

        Jugador currentJugador = getItem(position);
        imagen.setImageResource(Integer.parseInt(currentJugador.getColor()));
        nombre.setText(currentJugador.getNombre());
        if(currentJugador.getPuntos() >= 0){
            puntos.setVisibility(View.VISIBLE);
            puntos.setText(texto + " " + String.valueOf(currentJugador.getPuntos()));
        } else {
            puntos.setVisibility(View.GONE);
            nombre.setGravity(Gravity.CENTER_VERTICAL);
        }


        return convertView;
    }
}
