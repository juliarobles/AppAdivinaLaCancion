package gestion.informacion.appadivinalacancion.util.Otros;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import gestion.informacion.appadivinalacancion.R;

public class ListaJugadoresAdapter extends RecyclerView.Adapter<ListaJugadoresAdapter.MyViewHolder> implements View.OnClickListener {
    private List<JugadorProvisional> jugadores;
    private View.OnClickListener listener;

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
        public TextView nombre;
        public ImageView avatar;
        public MyViewHolder(View v) {
            super(v);
            nombre = v.findViewById(R.id.item_nombreJugador);
            avatar = v.findViewById(R.id.item_avatarJugador);
        }
    }

    public ListaJugadoresAdapter(List<JugadorProvisional> myDataset) {
        jugadores = myDataset;
    }

    @Override
    public ListaJugadoresAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_jugadoreslist, parent, false);
        v.setOnClickListener(this);
        return new ListaJugadoresAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ListaJugadoresAdapter.MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.nombre.setText(jugadores.get(position).getNombre());
        holder.avatar.setImageResource(jugadores.get(position).getColor());
    }

    @Override
    public int getItemCount() {
        return jugadores.size();
    }
}
