package com.example.kiwdy.ui.instructor.crear_curso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kiwdy.R;
import com.example.kiwdy.model.MaterialExtra;

import java.util.List;

public class MaterialExtraAdapter extends RecyclerView.Adapter<MaterialExtraAdapter.MaterialExtraViewHolder> {
    private List<MaterialExtra> materialesExtra;
    private Context context;
    private LayoutInflater layoutInflater;

    public MaterialExtraAdapter(List<MaterialExtra> materialesExtra, Context context, LayoutInflater layoutInflater) {
        this.materialesExtra = materialesExtra;
        this.context = context;
        this.layoutInflater = layoutInflater;
    }

    @NonNull
    @Override
    public MaterialExtraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_material_extra_lista, parent, false);
        return new MaterialExtraViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MaterialExtraViewHolder holder, int position) {

        MaterialExtra me = materialesExtra.get(position);
        holder.tvNombreArchivo.setText(me.getNombre());
    }

    @Override
    public int getItemCount() {
        return materialesExtra.size();
    }

    public class MaterialExtraViewHolder extends RecyclerView.ViewHolder{
        TextView tvNombreArchivo;

        public MaterialExtraViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreArchivo = itemView.findViewById(R.id.tvNombreArchivo);
        }
    }
}
