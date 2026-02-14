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

import com.example.kiwdy.R;
import com.example.kiwdy.api.dto.response.InscripcionResponse;
import com.example.kiwdy.databinding.FragmentProgresoAlumnoBinding;

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

        mViewModel.getmEstadoSolicitada().observe(getViewLifecycleOwner(), new Observer<InscripcionResponse>() {
            @Override
            public void onChanged(InscripcionResponse inscripcionResponse) {
                binding.tvNombreApellidoAlumnoProgreso.setText(inscripcionResponse.getUsuarioAlumno().getNombre() + " " + inscripcionResponse.getUsuarioAlumno().getApellido());
                binding.btAceptarInscripcionProgreso.setVisibility(View.VISIBLE);
                binding.btAceptarInscripcionProgreso.setOnClickListener(new View.OnClickListener() {
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
                binding.btAceptarInscripcionProgreso.setVisibility(View.GONE);
                binding.tvFechaInscriptoProgreso.setVisibility(View.VISIBLE);
                binding.textView5.setVisibility(View.VISIBLE);
                binding.progressBarProgreso.setVisibility(View.VISIBLE);
            }
        });
        mViewModel.getmProgreso().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                binding.progressBarProgreso.setProgress(integer);
            }
        });

        mViewModel.getmEstadoPendienteCertificacion().observe(getViewLifecycleOwner(), new Observer<InscripcionResponse>() {
            @Override
            public void onChanged(InscripcionResponse inscripcionResponse) {
                binding.textView5.setVisibility(View.VISIBLE);
                binding.progressBarProgreso.setVisibility(View.VISIBLE);
                binding.tvFechaInscriptoProgreso.setVisibility(View.VISIBLE);
                binding.btAgendarExamenProgreso.setVisibility(View.VISIBLE);

                binding.btAgendarExamenProgreso.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("inscripcion", inscripcionResponse);
                        Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main).navigate(R.id.agendarExamenFragment, bundle);
                    }
                });
                binding.btCertificarProgreso.setVisibility(View.VISIBLE);
                binding.btCertificarProgreso.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //mViewModel.certificar();
                    }
                });
            }
        });

        mViewModel.getmEstadoCertificada().observe(getViewLifecycleOwner(), new Observer<InscripcionResponse>() {
            @Override
            public void onChanged(InscripcionResponse inscripcionResponse) {
                binding.tvFechaInscriptoProgreso.setVisibility(View.VISIBLE);
                binding.tvFechaFinalizadoProgreso.setVisibility(View.VISIBLE);
                binding.ivCertificadoProgreso.setVisibility(View.VISIBLE);
                binding.btDescargarCertificadoProgreso.setVisibility(View.VISIBLE);
                binding.btDescargarCertificadoProgreso.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        });

        mViewModel.buscarInscripcion(getArguments());


        return binding.getRoot();
    }

}