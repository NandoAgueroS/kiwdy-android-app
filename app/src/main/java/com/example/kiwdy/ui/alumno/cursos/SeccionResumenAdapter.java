package com.example.kiwdy.ui.alumno.cursos;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kiwdy.R;
import com.example.kiwdy.api.dto.response.SeccionResponse;

import java.util.List;

public class SeccionResumenAdapter extends RecyclerView.Adapter<SeccionResumenAdapter.SeccionResumenViewHolder> {

    private List<SeccionResponse> secciones;
    private Context context;
    private LayoutInflater inflater;

    public SeccionResumenAdapter(List<SeccionResponse> secciones, Context context, LayoutInflater inflater) {
        this.secciones = secciones;
        this.context = context;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public SeccionResumenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_seccion_lista, parent, false);
        return new SeccionResumenViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SeccionResumenViewHolder holder, int position) {
        SeccionResponse seccion = secciones.get(position);
        holder.orden.setText(String.valueOf(seccion.getOrden()));
        holder.titulo.setText(seccion.getTitulo());
        Bundle bundle = new Bundle();
        bundle.putInt("orden", seccion.getOrden());
        bundle.putInt("idCurso", seccion.getIdCurso());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController((Activity) context, R.id.nav_host_fragment_content_alumno).navigate(R.id.detalleSeccionFragment, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return secciones.size();
    }

    public class SeccionResumenViewHolder extends RecyclerView.ViewHolder{
        TextView orden;
        TextView titulo;

        public SeccionResumenViewHolder(@NonNull View itemView) {
            super(itemView);
            orden = itemView.findViewById(R.id.tvOrdenSeccion);
            titulo = itemView.findViewById(R.id.tvNombreAlumnoItem);
        }
    }
}
