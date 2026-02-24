package com.example.kiwdy.ui.compartido.examenes;

import android.content.Context;
import android.content.DialogInterface;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kiwdy.R;
import com.example.kiwdy.api.dto.Modalidad;
import com.example.kiwdy.api.dto.response.ExamenResponse;
import com.example.kiwdy.api.utils.JwtUtil;
import com.example.kiwdy.api.utils.SharedPreferencesUtil;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ExamenAdapter extends RecyclerView.Adapter<ExamenAdapter.ExamenViewHolder> {
    private List<ExamenResponse> examenes;
    private Context context;
    private LayoutInflater layoutInflater;

    public interface OnClickListener{
        void onClickGuardarNota(DialogInterface dialog, View dialogNotaView, Button bt, int idExamen, String nota);
    }

    private OnClickListener listener;

    public ExamenAdapter(List<ExamenResponse> examenes, Context context, LayoutInflater layoutInflater, OnClickListener listener) {
        this.examenes= examenes;
        this.context = context;
        this.layoutInflater = layoutInflater;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExamenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_examen, parent, false);
        return new ExamenViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamenViewHolder holder, int position) {

        String rol = JwtUtil.obtenerRol(SharedPreferencesUtil.leerToken(context).replace("Bearer ", ""));

        ExamenResponse examen = examenes.get(position);
        holder.tvAlumnoExamenItem.setText(examen.getInscripcion().getUsuarioAlumno().getNombre() + " " + examen.getInscripcion().getUsuarioAlumno().getApellido());
        holder.tvCursoExamenItem.setText(examen.getInscripcion().getCurso().getTitulo());
        holder.tvFechaExamenItem.setText(LocalDate.from(examen.getFechaYHora()).toString());
        holder.tvHoraExamenItem.setText(LocalTime.from(examen.getFechaYHora()).toString());
        Modalidad modalidad = Modalidad.fromCodigo(examen.getModalidad());
        holder.tvModalidadExamenItem.setText(modalidad.toString());
        switch (modalidad){
            case VIRTUAL:
                holder.tvTituloLinkODireccionExamenItem.setText("Link: ");
                holder.tvLinkODireccionExamenItem.setText(examen.getLink());
                holder.tvLinkODireccionExamenItem.setAutoLinkMask(Linkify.WEB_URLS);
                holder.tvLinkODireccionExamenItem.setMovementMethod(LinkMovementMethod.getInstance());
            break;
            case PRESENCIAL:
                holder.tvTituloLinkODireccionExamenItem.setText("Dirección: ");
                holder.tvLinkODireccionExamenItem.setText(examen.getDireccion());
                break;
        }
        if (rol.equals("Instructor") && examen.getNota() == -1) {
            holder.btFinalizarExamenItem.setVisibility(View.VISIBLE);

            holder.btFinalizarExamenItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View dialogView = layoutInflater.inflate(R.layout.dialog_input_nota, null);
                    TextInputEditText etNotaExamenDialog = dialogView.findViewById(R.id.etNotaExamenDialog);

                    AlertDialog dialog = new MaterialAlertDialogBuilder(context)
                            .setTitle("Finalizar exámen")
                            .setMessage("Ingrese la nota del exámen")
                            .setView(dialogView)
                            .setPositiveButton("Guardar", null)
                            .setNegativeButton("Cancelar", null)
                            .create();
                    dialog.show();

                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.onClickGuardarNota(dialog, dialogView, holder.btFinalizarExamenItem, examen.getIdExamen(), etNotaExamenDialog.getText().toString());
                        }
                    });
                }
            });
        }else if (examen.getNota() != -1){
            holder.tvNotaLabelExamenItem.setVisibility(View.VISIBLE);
            holder.tvNotaExamenItem.setText(examen.getNota() + "");
            holder.tvNotaExamenItem.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return examenes.size();
    }

    public class ExamenViewHolder extends RecyclerView.ViewHolder{
        TextView tvAlumnoExamenItem, tvCursoExamenItem, tvFechaExamenItem, tvHoraExamenItem, tvModalidadExamenItem, tvLinkODireccionExamenItem, tvTituloLinkODireccionExamenItem, tvNotaLabelExamenItem, tvNotaExamenItem;
        Button btFinalizarExamenItem;

        public ExamenViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAlumnoExamenItem = itemView.findViewById(R.id.tvAlumnoExamenItem);
            tvCursoExamenItem= itemView.findViewById(R.id.tvCursoExamenItem);
            tvFechaExamenItem = itemView.findViewById(R.id.tvFechaExamenItem);
            tvHoraExamenItem = itemView.findViewById(R.id.tvHoraExamenItem);
            tvModalidadExamenItem = itemView.findViewById(R.id.tvModalidadExamenItem);
            tvNotaLabelExamenItem= itemView.findViewById(R.id.tvNotaLabelExamenItem);
            tvNotaExamenItem = itemView.findViewById(R.id.tvNotaExamenItem);
            tvTituloLinkODireccionExamenItem = itemView.findViewById(R.id.tvTituloLinkODireccionExamenItem);
            tvLinkODireccionExamenItem = itemView.findViewById(R.id.tvLinkODireccionExamenItem);
            btFinalizarExamenItem = itemView.findViewById(R.id.btFinalizarExamenItem);
        }
    }
}
