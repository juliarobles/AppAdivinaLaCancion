package gestion.informacion.appadivinalacancion.util.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import gestion.informacion.appadivinalacancion.R;
import gestion.informacion.appadivinalacancion.util.Modelo.Jugador;


public class PuntosAdapter extends RecyclerView.Adapter<PuntosAdapter.MyViewHolder> {
    private List<Jugador> mDataset;
    private String textPuntos, textAcertadas;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_jugadores_resultado, parent, false);
        return new PuntosAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Jugador j = this.mDataset.get(position);
        holder.icono.setImageResource(Integer.parseInt(j.getColor()));
        holder.nombre.setText(j.getNombre());
        holder.acertadas.setText(textAcertadas + " " + j.getAcertadas());
        holder.puntos.setText(textPuntos + " " + j.getPuntos() + "");
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
    public PuntosAdapter(List<Jugador> datos, String textPuntos, String textAcertadas){
        this.mDataset = datos;
        this.textPuntos = textPuntos;
        this.textAcertadas = textAcertadas;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView puntos, nombre, acertadas;
        public ImageView icono;
        public MyViewHolder(View v) {
            super(v);
            puntos = v.findViewById(R.id.puntosJugadorResultado);
            nombre = v.findViewById(R.id.nombreJugadorResultado);
            icono = v.findViewById(R.id.iconoJugador);
            acertadas = v.findViewById(R.id.textAcertadas);
        }
    }

}
