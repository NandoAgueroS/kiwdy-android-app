package com.example.kiwdy.ui.alumno.mis_inscripciones;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kiwdy.R;
import com.example.kiwdy.api.dto.response.CursoResponse;
import com.example.kiwdy.databinding.FragmentMisIncripcionesBinding;
import com.example.kiwdy.ui.compartido.UIDialogs;
import com.example.kiwdy.ui.compartido.inicio.CursoAdapter;
import com.example.kiwdy.ui.compartido.login.LoginActivity;

import java.util.List;

public class MisIncripcionesFragment extends Fragment {

    private MisIncripcionesViewModel mViewModel;
    private FragmentMisIncripcionesBinding binding;

    public static MisIncripcionesFragment newInstance() {
        return new MisIncripcionesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(MisIncripcionesViewModel.class);
        binding = FragmentMisIncripcionesBinding.inflate(inflater, container, false);

        mViewModel.getmSesionInvalida().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("desde_sesion_expirada", true);
                startActivity(intent);
            }
        });

        mViewModel.getmError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                UIDialogs.error(requireContext(), s);
            }
        });

        mViewModel.getmCursos().observe(getViewLifecycleOwner(), new Observer<List<CursoResponse>>() {
            @Override
            public void onChanged(List<CursoResponse> cursoResponses) {
                CursoAdapter adapter = new CursoAdapter(cursoResponses, requireContext(), inflater);

                GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 2);

                binding.rvCursosMisInscripciones.setLayoutManager(layoutManager);
                binding.rvCursosMisInscripciones.setAdapter(adapter);
            }
        });
        binding.etBuscarTituloCursoMisInscripciones.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mViewModel.buscarInscripcionesPorTitulo(s.toString());
            }
        });
        mViewModel.buscarInscripcionesPorTitulo("");


        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        mViewModel.buscarInscripcionesPorTitulo(binding.etBuscarTituloCursoMisInscripciones.getText().toString());
    }
}