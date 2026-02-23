package com.example.kiwdy.ui.instructor.borradores;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import com.example.kiwdy.model.CursoLocal;

import java.util.List;

public class BorradorCursoAdapter extends RecyclerView.Adapter<BorradorCursoAdapter.BorradorCursoViewHolder>{

    private List<CursoLocal> cursos;
    private Context context;
    private LayoutInflater layoutInflater;

    public BorradorCursoAdapter(List<CursoLocal> cursos, Context context, LayoutInflater layoutInflater) {
        this.cursos = cursos;
        this.context = context;
        this.layoutInflater = layoutInflater;
    }

    @NonNull
    @Override
    public BorradorCursoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_curso_lista, parent, false);
        return new BorradorCursoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BorradorCursoViewHolder holder, int position) {
        CursoLocal curso = cursos.get(position);

        holder.tvTituloCurso.setText(curso.getTitulo());
        holder.tvDescripcionCurso.setText(curso.getDescripcion());

        holder.ivPortadaCurso.setImageURI(Uri.parse(curso.getPortadaUri()));
        Bundle bundle = new Bundle();
        bundle.putString("nombreArchivoBorrador", curso.getNombreArchivoBorrador());
        Log.d("TOKEN", SharedPreferencesUtil.leerToken(context));

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Navigation.findNavController((Activity) v.getContext(), R.id.nav_host_fragment_content_main).navigate(R.id.crearCursoFragment, bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        return cursos.size();
    }

    public class BorradorCursoViewHolder extends RecyclerView.ViewHolder{
        View item;
        TextView tvTituloCurso;
        TextView tvDescripcionCurso;
        ImageView ivPortadaCurso;
        ImageButton btVerInscriptos;

        public BorradorCursoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTituloCurso = itemView.findViewById(R.id.tvAlumnoExamenItem);
            tvDescripcionCurso = itemView.findViewById(R.id.tvDescripcionCursoItem);
            ivPortadaCurso = itemView.findViewById(R.id.ivPortadaCursoItem);
            btVerInscriptos = itemView.findViewById(R.id.btVerInscriptosItem);
            item = itemView;
        }
    }
}
