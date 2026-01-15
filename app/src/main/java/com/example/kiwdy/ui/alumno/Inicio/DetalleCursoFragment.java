package com.example.kiwdy.ui.alumno.Inicio;

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
import android.widget.Adapter;

import com.bumptech.glide.Glide;
import com.example.kiwdy.R;
import com.example.kiwdy.api.ApiClient;
import com.example.kiwdy.api.dto.response.CursoResponse;
import com.example.kiwdy.databinding.FragmentDetalleCursoBinding;
import com.example.kiwdy.ui.compartido.inicio.CursoAdapter;

public class DetalleCursoFragment extends Fragment {

    private DetalleCursoViewModel mViewModel;
    private FragmentDetalleCursoBinding binding;

    public static DetalleCursoFragment newInstance() {
        return new DetalleCursoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(DetalleCursoViewModel.class);
        binding = FragmentDetalleCursoBinding.inflate(inflater, container, false);

        mViewModel.getmCurso().observe(getViewLifecycleOwner(), new Observer<CursoResponse>() {
            @Override
            public void onChanged(CursoResponse cursoResponse) {
                binding.etTituloDetalleCurso.setText(cursoResponse.getTitulo());
                binding.etDescripcionDetalleCurso.setText(cursoResponse.getDescripcion());

                Glide.with(requireContext())
                        .load(ApiClient.URL_BASE + cursoResponse.getPortadaUrl())
                        .placeholder(R.drawable.fondo)
                        .error(R.drawable.fondo)
                        .into(binding.ivPortadaCursoDetalleCurso);
                SeccionResumenAdapter adapter = new SeccionResumenAdapter(cursoResponse.getSecciones(), requireContext(), getLayoutInflater());
                GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false);
                binding.rvSeccionesDetalleCurso.setLayoutManager(layoutManager);
                binding.rvSeccionesDetalleCurso.setAdapter(adapter);

            }

        });

        mViewModel.mostrarCurso(getArguments());

        binding.btInscribirseDetalleCurso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.inscribir();
            }
        });


        return binding.getRoot();
    }

}