package gestion.informacion.appadivinalacancion.util.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

import gestion.informacion.appadivinalacancion.R;
import gestion.informacion.appadivinalacancion.util.BBDD.BBDD_Struct;
import gestion.informacion.appadivinalacancion.util.Modelo.Jugador;
import gestion.informacion.appadivinalacancion.util.Modelo.Partida;
import gestion.informacion.appadivinalacancion.util.Otros.JugadorProvisional;

public class ListaHistorialAdapter extends RecyclerView.Adapter<ListaHistorialAdapter.MyViewHolder> implements View.OnClickListener {
    private List<Partida> historial;
    private View.OnClickListener listener;
    private String textGanador, textRondas;

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(listener != null){
            listener.onClick(view);
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView ganador, fecha, rondas, playlist;
        public ImageView avatar;
        public MyViewHolder(View v) {
            super(v);
            ganador = v.findViewById(R.id.textGanadorHistorial);
            avatar = v.findViewById(R.id.avatarGanadorHistorial);
            fecha = v.findViewById(R.id.textFechaHistorial);
            rondas = v.findViewById(R.id.textNumRondasHistorial);
            playlist = v.findViewById(R.id.textPlaylistHistorial);
        }
    }

    public ListaHistorialAdapter(List<Partida> myDataset, String textRondas, String textGanador) {
        historial = myDataset;
        this.textGanador = textGanador;
        this.textRondas = textRondas;
    }

    @Override
    public ListaHistorialAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_historiallist, parent, false);
        v.setOnClickListener(this);
        return new ListaHistorialAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ListaHistorialAdapter.MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Partida p = historial.get(position);
        System.out.println(p.getFecha().toString());
        holder.fecha.setText(BBDD_Struct.formatoFecha.format(p.getFecha()));
        holder.playlist.setText(p.getPlaylist().getNombre());
        holder.rondas.setText(textRondas + " " + String.valueOf(p.getRondas()));
        Picasso.get().load(p.getPlaylist().getImagen().toString()).into(holder.avatar);
        Jugador gan = historial.get(position).getGanador();
        if(gan != null){
            holder.ganador.setText(textGanador + " " + p.getGanador().getNombre());
        } else {
            holder.ganador.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return historial.size();
    }
}
