package com.example.kiwdy.ui.compartido.inicio;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kiwdy.R;
import com.example.kiwdy.api.ApiClient;
import com.example.kiwdy.api.dto.response.CursoResponse;
import com.example.kiwdy.api.utils.JwtUtil;
import com.example.kiwdy.api.utils.SharedPreferencesUtil;

import java.util.List;

public class CursoAdapter extends RecyclerView.Adapter<CursoAdapter.CursoViewHolder>{

    private List<CursoResponse> cursos;
    private Context context;
    private LayoutInflater layoutInflater;

    public CursoAdapter(List<CursoResponse> cursos, Context context, LayoutInflater layoutInflater) {
        this.cursos = cursos;
        this.context = context;
        this.layoutInflater = layoutInflater;
    }

    @NonNull
    @Override
    public CursoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_curso_lista, parent, false);
        return new CursoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CursoViewHolder holder, int position) {
        CursoResponse curso = cursos.get(position);

        holder.tvTituloCurso.setText(curso.getTitulo());
        holder.tvDescripcionCurso.setText(curso.getDescripcion());

        Glide.with(context)
                .load(ApiClient.URL_BASE + curso.getPortadaUrl())
                .placeholder(R.drawable.fondo)
                .error(R.drawable.fondo)
                .into(holder.ivPortadaCurso);
        Bundle bundle = new Bundle();
        bundle.putSerializable("idCurso", curso.getIdCurso());
        Log.d("TOKEN", SharedPreferencesUtil.leerToken(context));
        String rol = JwtUtil.obtenerRol(SharedPreferencesUtil.leerToken(context).replace("Bearer ", ""));

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (rol){
                    case "Instructor": Navigation.findNavController((Activity) v.getContext(), R.id.nav_host_fragment_content_main).navigate(R.id.crearCursoFragment, bundle);
                        break;
                    case "Alumno": Navigation.findNavController((Activity) v.getContext(), R.id.nav_host_fragment_content_alumno).navigate(R.id.detalleCursoFragment, bundle);
                        break;
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return cursos.size();
    }

    public class CursoViewHolder extends RecyclerView.ViewHolder{
        View item;
        TextView tvTituloCurso;
        TextView tvDescripcionCurso;
        ImageView ivPortadaCurso;

        public CursoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTituloCurso = itemView.findViewById(R.id.tvTituloCursoItem);
            tvDescripcionCurso = itemView.findViewById(R.id.tvDescripcionCursoItem);
            ivPortadaCurso = itemView.findViewById(R.id.ivPortadaCursoItem);
            item = itemView;
        }
    }
}
