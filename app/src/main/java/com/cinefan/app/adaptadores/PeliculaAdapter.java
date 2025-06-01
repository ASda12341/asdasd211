package com.cinefan.app.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.cinefan.app.R;
import com.cinefan.app.modelos.Pelicula;
import java.util.List;

public class PeliculaAdapter extends RecyclerView.Adapter<PeliculaAdapter.ViewHolder> {
    
    private List<Pelicula> peliculas;
    private OnPeliculaListener listener;
    
    public interface OnPeliculaListener {
        void onPeliculaClick(int position);
        void onPeliculaLongClick(int position);
    }
    
    public PeliculaAdapter(List<Pelicula> peliculas, OnPeliculaListener listener) {
        this.peliculas = peliculas;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pelicula, parent, false);
        return new ViewHolder(view, listener);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pelicula pelicula = peliculas.get(position);
        
        // muestra los datos de la pelicula
        holder.textoTitulo.setText(pelicula.getTitulo());
        holder.textoDirector.setText(pelicula.getDirector());
        holder.textoAnio.setText(String.valueOf(pelicula.getAnio()));
        holder.textoGenero.setText(pelicula.getGenero());
    }
    
    @Override
    public int getItemCount() {
        return peliculas.size();
    }
    
    // clase viewholder para las peliculas
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView textoTitulo, textoDirector, textoAnio, textoGenero;
        OnPeliculaListener listener;
        
        public ViewHolder(@NonNull View itemView, OnPeliculaListener listener) {
            super(itemView);
            textoTitulo = itemView.findViewById(R.id.texto_titulo);
            textoDirector = itemView.findViewById(R.id.texto_director);
            textoAnio = itemView.findViewById(R.id.texto_anio);
            textoGenero = itemView.findViewById(R.id.texto_genero);
            
            this.listener = listener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }
        
        @Override
        public void onClick(View v) {
            listener.onPeliculaClick(getAdapterPosition());
        }
        
        @Override
        public boolean onLongClick(View v) {
            listener.onPeliculaLongClick(getAdapterPosition());
            return true;
        }
    }
}