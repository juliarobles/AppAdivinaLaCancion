package gestion.informacion.appadivinalacancion.util.Otros;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import gestion.informacion.appadivinalacancion.R;

public class ListaPlaylistAdapter extends RecyclerView.Adapter<ListaPlaylistAdapter.MyViewHolder> implements View.OnClickListener {
    private List<Tupla> datos;
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

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView nombre;
        public TextView propietario;
        public ImageView imagen;
        public MyViewHolder(View v) {
            super(v);
            nombre = v.findViewById(R.id.item_nombrePlaylist);
            propietario = v.findViewById(R.id.item_propPlaylist);
            imagen = v.findViewById(R.id.item_avatarPlaylist);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ListaPlaylistAdapter(List<Tupla> myDataset) {
        datos = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ListaPlaylistAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_playlistlist, parent, false);
        v.setOnClickListener(this);
        return new MyViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Tupla t = datos.get(position);
        holder.nombre.setText(t.getX1());
        holder.propietario.setText(t.getProp());
        Picasso.get().load(t.getImagen()).into(holder.imagen);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return datos.size();
    }
}
