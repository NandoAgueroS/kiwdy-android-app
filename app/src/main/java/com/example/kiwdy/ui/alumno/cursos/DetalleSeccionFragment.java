package com.example.kiwdy.ui.alumno.cursos;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.kiwdy.R;
import com.example.kiwdy.api.dto.response.CursoInscripcionResponse;
import com.example.kiwdy.api.dto.response.CursoResponse;
import com.example.kiwdy.api.dto.response.InscripcionResponse;
import com.example.kiwdy.api.dto.response.SeccionResponse;
import com.example.kiwdy.databinding.FragmentDetalleSeccionBinding;
import com.example.kiwdy.ui.compartido.UIDialogs;

public class DetalleSeccionFragment extends Fragment {

    private DetalleSeccionViewModel mViewModel;
    private FragmentDetalleSeccionBinding binding;

    public static DetalleSeccionFragment newInstance() {
        return new DetalleSeccionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(DetalleSeccionViewModel.class);
        binding = FragmentDetalleSeccionBinding.inflate(inflater, container, false);

        mViewModel.getmError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String mensaje) {
                UIDialogs.error(requireContext(), mensaje);
            }
        });
        mViewModel.getmMostrarBotonSiguiente().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.btSiguienteSeccionDetalle.setEnabled(aBoolean);
            }
        });
        mViewModel.getmMostrarBotonAnterior().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.btAnteriorSeccionDetalle.setEnabled(aBoolean);
            }
        });
        mViewModel.getmInscripcion().observe(getViewLifecycleOwner(), new Observer<InscripcionResponse>() {
            @Override
            public void onChanged(InscripcionResponse inscripcionResponse) {
                mViewModel.cargarSeccion(getArguments());
            }
        });
        mViewModel.getmSeccion().observe(getViewLifecycleOwner(), new Observer<SeccionResponse>() {
            @Override
            public void onChanged(SeccionResponse seccionResponse) {
                binding.tvTituloSeccionDetalle.setText(seccionResponse.getTitulo());
                binding.tvContenidoSeccionDetalle.setText(seccionResponse.getContenido());
                VideoView videoView = binding.vvSeccionDetalle;
                videoView.setVideoPath("" + seccionResponse.getVideoUrl());
                MediaController mediaController = new MediaController(requireContext());
                mediaController.setAnchorView(videoView);
                videoView.setMediaController(mediaController);

                binding.btMarcarCompletadaDetalle.setEnabled(true);
                //videoView.start();

                DetalleArchivosSeccionAdapter adapter =  new DetalleArchivosSeccionAdapter(seccionResponse.getMateriales(), requireContext(),inflater);
                GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false);
                binding.rvMaterialesExtraDetalle.setLayoutManager(layoutManager);
                binding.rvMaterialesExtraDetalle.setAdapter(adapter);

            }
        });

        mViewModel.getmMostrarBotonMarcarCompletada().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.btMarcarCompletadaDetalle.setEnabled(aBoolean);
            }
        });

        mViewModel.recuperarCurso(getArguments());

        binding.btMarcarCompletadaDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.marcarCompletada();
            }
        });

        binding.btAnteriorSeccionDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.retrocederSeccion();
            }
        });
        binding.btSiguienteSeccionDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.siguienteSeccion();
            }
        });

        return binding.getRoot();
    }
}