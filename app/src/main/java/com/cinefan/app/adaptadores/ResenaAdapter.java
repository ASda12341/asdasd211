package com.cinefan.app.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.cinefan.app.R;
import com.cinefan.app.modelos.Resena;
import java.util.List;

public class ResenaAdapter extends RecyclerView.Adapter<ResenaAdapter.ViewHolder> {
    
    private List<Resena> resenas;
    private Context contexto;
    
    public ResenaAdapter(List<Resena> resenas, Context contexto) {
        this.resenas = resenas;
        this.contexto = contexto;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(contexto).inflate(R.layout.item_resena, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Resena resena = resenas.get(position);
        
        // muestra los datos de la resena
        holder.textoPelicula.setText(resena.getTituloPelicula());
        holder.textoUsuario.setText("@" + resena.getNombreUsuario());
        holder.textoComentario.setText(resena.getComentario());
        holder.textoFecha.setText(resena.getFecha());
        holder.ratingBar.setRating(resena.getPuntuacion());
    }
    
    @Override
    public int getItemCount() {
        return resenas.size();
    }
    
    // clase viewholder para las resenas
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textoPelicula, textoUsuario, textoComentario, textoFecha;
        RatingBar ratingBar;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textoPelicula = itemView.findViewById(R.id.texto_pelicula);
            textoUsuario = itemView.findViewById(R.id.texto_usuario);
            textoComentario = itemView.findViewById(R.id.texto_comentario);
            textoFecha = itemView.findViewById(R.id.texto_fecha);
            ratingBar = itemView.findViewById(R.id.rating_bar);
        }
    }
}