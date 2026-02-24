package com.example.kiwdy.ui.instructor.inscripciones;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kiwdy.R;
import com.example.kiwdy.api.dto.response.InscripcionResponse;
import com.example.kiwdy.databinding.FragmentInscripcionesBinding;
import com.example.kiwdy.ui.compartido.UIDialogs;
import com.example.kiwdy.ui.compartido.login.LoginActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.List;

public class InscripcionesFragment extends Fragment {

    private InscripcionesViewModel mViewModel;
    private FragmentInscripcionesBinding binding;

    public static InscripcionesFragment newInstance() {
        return new InscripcionesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(InscripcionesViewModel.class);
        binding = FragmentInscripcionesBinding.inflate(inflater, container, false);

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
        mViewModel.getmIdCurso().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                mViewModel.listarInscripciones("0", true);
            }
        });
        mViewModel.getmInscripciones().observe(getViewLifecycleOwner(), new Observer<List<InscripcionResponse>>() {
            @Override
            public void onChanged(List<InscripcionResponse> inscripcionResponses) {
                InscripcionAdapter adapter = new InscripcionAdapter(inscripcionResponses, requireContext(), inflater);
                GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false);
                binding.rvInscripciones.setLayoutManager(layoutManager);
                binding.rvInscripciones.setAdapter(adapter);
            }
        });

        mViewModel.getmMostrandoInscripcionesPorCurso().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.tvCursoLabelInscripciones.setText("Inscripciones del curso:");
                binding.tvCursoInscripciones.setVisibility(View.VISIBLE);
                binding.tvCursoInscripciones.setText(s);
            }
        });
        mViewModel.getmMostrandoTodasLasInscripciones().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.tvCursoLabelInscripciones.setText("Todas las inscripciones");
                mViewModel.listarInscripciones("0", true);
            }
        });

        binding.btGroupInscripciones.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup materialButtonToggleGroup, int i, boolean b) {
                MaterialButton button = getActivity().findViewById(i);
                mViewModel.listarInscripciones(button.getTag().toString(), b);
            }
        });
        mViewModel.recuperarIdCurso(getArguments());

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}