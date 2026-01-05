package com.example.kiwdy.ui.compartido.inicio;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kiwdy.R;
import com.example.kiwdy.api.dto.response.CursoResponse;
import com.example.kiwdy.databinding.FragmentInicioBinding;

import java.util.List;

public class InicioFragment extends Fragment {

    private InicioViewModel mViewModel;
    private FragmentInicioBinding binding;

    public static InicioFragment newInstance() {
        return new InicioFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(InicioViewModel.class);
        binding = FragmentInicioBinding.inflate(inflater, container, false);

        binding.btCrearCurso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main).navigate(R.id.crearCursoFragment);
            }
        });

        mViewModel.getmCursos().observe(getViewLifecycleOwner(), new Observer<List<CursoResponse>>() {
            @Override
            public void onChanged(List<CursoResponse> cursoResponses) {
                CursoAdapter adapter = new CursoAdapter(cursoResponses, requireContext(), getLayoutInflater());
                GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false);
                binding.rvCursosInicio.setLayoutManager(gridLayoutManager);
                binding.rvCursosInicio.setAdapter(adapter);
            }
        });

        mViewModel.cargarListaCursos();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

}