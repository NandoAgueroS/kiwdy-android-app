package com.example.kiwdy.ui.compartido;

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

import com.example.kiwdy.R;
import com.example.kiwdy.api.dto.response.CursoResponse;
import com.example.kiwdy.databinding.FragmentCursosBinding;
import com.example.kiwdy.ui.compartido.inicio.CursoAdapter;
import com.example.kiwdy.ui.compartido.inicio.InicioViewModel;

import java.util.List;

public class CursosFragment extends Fragment {

    private CursosViewModel mViewModel;
    private FragmentCursosBinding binding;

    public static CursosFragment newInstance() {
        return new CursosFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCursosBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(CursosViewModel.class);
        mViewModel.getmCursos().observe(getViewLifecycleOwner(), new Observer<List<CursoResponse>>() {
            @Override
            public void onChanged(List<CursoResponse> cursoResponses) {
                CursoAdapter adapter = new CursoAdapter(cursoResponses, requireContext(), getLayoutInflater());
                GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);
                binding.rvCursos.setLayoutManager(gridLayoutManager);
                binding.rvCursos.setAdapter(adapter);
            }
        });
        mViewModel.cargarListaCursos();

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}