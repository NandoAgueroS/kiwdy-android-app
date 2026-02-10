package com.example.kiwdy.ui.instructor.inscripciones;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kiwdy.R;
import com.example.kiwdy.api.dto.EstadoInscripcion;
import com.example.kiwdy.api.dto.response.InscripcionResponse;

import java.util.List;

public class InscripcionAdapter extends RecyclerView.Adapter<InscripcionAdapter.InscripcionViewHolder> {
    private List<InscripcionResponse> inscripciones;
    private Context context;
    private LayoutInflater layoutInflater;

    public InscripcionAdapter(List<InscripcionResponse> inscripciones, Context context, LayoutInflater layoutInflater) {
        this.inscripciones= inscripciones;
        this.context = context;
        this.layoutInflater = layoutInflater;
    }

    @NonNull
    @Override
    public InscripcionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_inscripcion_lista, parent, false);
        return new InscripcionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InscripcionViewHolder holder, int position) {

        InscripcionResponse inscripcion = inscripciones.get(position);
        holder.tvNombreAlumno.setText(inscripcion.getUsuarioAlumno().getNombre() + " " + inscripcion.getUsuarioAlumno().getApellido());
        if (inscripcion.getFechaInicio() != null) holder.tvFecha.setText(inscripcion.getFechaInicio().toString());
        String estado = inscripcion.getEstado();

        switch (estado){
            case "Solicitada" : holder.btAccion.setText("Aceptar");
            break;
            case "EnCurso" : holder.btAccion.setText("Ver");
                break;
            case "PendienteCertificacion" : holder.btAccion.setText("Certificar");
                break;
            case "Certificada" : holder.btAccion.setText("Ver");
                break;
        }

        Bundle bundle = new Bundle();
        bundle.putInt("idInscripcion", inscripcion.getIdInscripcion());
        holder.btAccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController((Activity) context, R.id.nav_host_fragment_content_main).navigate(R.id.progresoAlumnoFragment, bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        return inscripciones.size();
    }

    public class InscripcionViewHolder extends RecyclerView.ViewHolder{
        TextView tvNombreAlumno;
        TextView tvFecha;
        Button btAccion;

        public InscripcionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreAlumno= itemView.findViewById(R.id.tvNombreAlumnoItem);
            btAccion = itemView.findViewById(R.id.btAccionInscriptoItem);
        }
    }
}
