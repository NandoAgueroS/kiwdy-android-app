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
    private DetalleArchivosSeccionAdapter.OnClickListener onClickListener;

    public DetalleArchivosSeccionAdapter(List<ArchivoSeccionResponse> archivos, Context context, LayoutInflater layoutInflater, DetalleArchivosSeccionAdapter.OnClickListener onClickListener) {
        this.archivos = archivos;
        this.context = context;
        this.layoutInflater = layoutInflater;
        this.onClickListener = onClickListener;
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
        holder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickListener.onClick(archivo.getIdMaterial(), archivo.getNombre());
                    }
                }
        );
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

    public interface OnClickListener {
        void onClick(int idMaterial, String nombreMaterial);
    }
}
