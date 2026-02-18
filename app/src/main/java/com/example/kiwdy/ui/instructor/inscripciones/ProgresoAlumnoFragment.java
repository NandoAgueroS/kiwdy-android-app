package com.example.kiwdy.ui.instructor.inscripciones;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kiwdy.R;
import com.example.kiwdy.api.dto.response.InscripcionResponse;
import com.example.kiwdy.databinding.FragmentProgresoAlumnoBinding;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class ProgresoAlumnoFragment extends Fragment {

    private ProgresoAlumnoViewModel mViewModel;
    private FragmentProgresoAlumnoBinding binding;

    public static ProgresoAlumnoFragment newInstance() {
        return new ProgresoAlumnoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(ProgresoAlumnoViewModel.class);
        binding = FragmentProgresoAlumnoBinding.inflate(inflater, container, false);

        TextView tvFechaInscriptoLabel = binding.tvFechaInscriptoLabelProgreso;
        TextView tvFechaFinalizadoLabel = binding.tvFechaFinalizadoLabelProgreso;
        TextView tvFechaInscripto = binding.tvFechaInscriptoProgreso;
        TextView tvFechaFinalizado = binding.tvFechaFinalizadoProgreso;
        ProgressBar progressBar = binding.progressBarProgreso;
        TextView tvNombreApellidoAlumno = binding.tvNombreApellidoAlumnoProgreso;
        TextView tvProgresoLabel = binding.tvProgreso;
        PDFView pdfViewCertificado = binding.pdfViewCertificadoProgreso;
        Button btAceptarInscripcion = binding.btAceptarInscripcionProgreso;
        Button btDescargarCertificado = binding.btDescargarCertificadoProgreso;
        Button btAgendarExamen = binding.btAgendarExamenProgreso;

        mViewModel.getmEstadoSolicitada().observe(getViewLifecycleOwner(), new Observer<InscripcionResponse>() {
            @Override
            public void onChanged(InscripcionResponse inscripcionResponse) {
               tvNombreApellidoAlumno.setText(inscripcionResponse.getUsuarioAlumno().getNombre() + " " + inscripcionResponse.getUsuarioAlumno().getApellido());
                btAceptarInscripcion.setVisibility(View.VISIBLE);
                btAceptarInscripcion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewModel.aceptarInscripcion();
                    }
                });
            }
        });

        mViewModel.getmEstadoEnCurso().observe(getViewLifecycleOwner(), new Observer<InscripcionResponse>() {
            @Override
            public void onChanged(InscripcionResponse inscripcionResponse) {
                btAceptarInscripcion.setVisibility(View.GONE);
                tvFechaInscriptoLabel.setVisibility(View.GONE);
                tvFechaInscripto.setVisibility(View.VISIBLE);
                tvProgresoLabel.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
            }
        });
        mViewModel.getmProgreso().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                progressBar.setProgress(integer);
            }
        });

        mViewModel.getmEstadoPendienteCertificacion().observe(getViewLifecycleOwner(), new Observer<InscripcionResponse>() {
            @Override
            public void onChanged(InscripcionResponse inscripcionResponse) {
                tvProgresoLabel.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                tvFechaInscripto.setVisibility(View.VISIBLE);
                tvFechaInscriptoLabel.setVisibility(View.VISIBLE);
                btAgendarExamen.setVisibility(View.VISIBLE);

                btAgendarExamen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("inscripcion", inscripcionResponse);
                        Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main).navigate(R.id.agendarExamenFragment, bundle);
                    }
                });
            }
        });

        mViewModel.getmEstadoCertificada().observe(getViewLifecycleOwner(), new Observer<InscripcionResponse>() {
            @Override
            public void onChanged(InscripcionResponse inscripcionResponse) {
                tvFechaInscripto.setVisibility(View.VISIBLE);
                tvFechaInscriptoLabel.setVisibility(View.VISIBLE);
                tvFechaFinalizado.setVisibility(View.VISIBLE);
                tvFechaFinalizadoLabel.setVisibility(View.VISIBLE);
                pdfViewCertificado.setVisibility(View.VISIBLE);
                btDescargarCertificado.setVisibility(View.VISIBLE);
                btDescargarCertificado.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        mViewModel.guardarCertificado();
                    }
                });
            }
        });

        mViewModel.getmCertificadoPdf().observe(getViewLifecycleOwner(), new Observer<File>() {
            @Override
            public void onChanged(File file) {
                pdfViewCertificado.fromFile(file)
                        .enableSwipe(true)
                        .swipeHorizontal(true)
                        .enableDoubletap(true)
                        .load();
            }
        });

        mViewModel.buscarInscripcion(getArguments());


        return binding.getRoot();
    }

}