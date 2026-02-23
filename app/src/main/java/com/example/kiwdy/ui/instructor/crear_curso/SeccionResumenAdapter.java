package com.example.kiwdy.ui.instructor.crear_curso;

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
import com.example.kiwdy.model.CursoLocal;
import com.example.kiwdy.model.SeccionLocal;

import java.util.List;

public class SeccionResumenAdapter extends RecyclerView.Adapter<SeccionResumenAdapter.SeccionResumenViewHolder> {

    private List<SeccionLocal> secciones;
    private Context context;
    private LayoutInflater inflater;
    private OnClickListener onClickListener;

    public SeccionResumenAdapter(List<SeccionLocal> secciones, Context context, LayoutInflater inflater, OnClickListener onClickListener) {
        this.secciones = secciones;
        this.context = context;
        this.inflater = inflater;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public SeccionResumenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_seccion_lista, parent, false);
        return new SeccionResumenViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SeccionResumenViewHolder holder, int position) {
        SeccionLocal seccion = secciones.get(position);
        holder.orden.setText(String.valueOf(seccion.getOrden()));
        holder.titulo.setText(seccion.getTitulo());

        Bundle bundle = new Bundle();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(seccion.getOrden());
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
            titulo = itemView.findViewById(R.id.tvAlumnoExamenItem);
        }
    }

    public interface OnClickListener{
        void onClick(int orden);
    }
}
