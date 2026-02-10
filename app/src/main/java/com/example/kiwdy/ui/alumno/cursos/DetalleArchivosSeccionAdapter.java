package com.example.kiwdy.ui.alumno.cursos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kiwdy.R;
import com.example.kiwdy.api.dto.response.ArchivoSeccionResponse;

import java.util.List;

public class DetalleArchivosSeccionAdapter extends RecyclerView.Adapter<DetalleArchivosSeccionAdapter.ArchivoSeccionViewHolder> {
    private List<ArchivoSeccionResponse> archivos;
    private Context context;
    private LayoutInflater layoutInflater;

    public DetalleArchivosSeccionAdapter(List<ArchivoSeccionResponse> archivos, Context context, LayoutInflater layoutInflater) {
        this.archivos = archivos;
        this.context = context;
        this.layoutInflater = layoutInflater;
    }

    @NonNull
    @Override
    public ArchivoSeccionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_material_extra_lista, parent, false);
        return new ArchivoSeccionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ArchivoSeccionViewHolder holder, int position) {

        ArchivoSeccionResponse archivo = archivos.get(position);
        holder.tvNombreArchivo.setText(archivo.getNombre());
    }

    @Override
    public int getItemCount() {
        return archivos.size();
    }

    public class ArchivoSeccionViewHolder extends RecyclerView.ViewHolder{
        TextView tvNombreArchivo;

        public ArchivoSeccionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreArchivo = itemView.findViewById(R.id.tvNombreArchivoItem);
        }
    }
}
