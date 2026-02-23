package com.example.kiwdy.ui.instructor.borradores;

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
import com.example.kiwdy.databinding.FragmentBorradoresBinding;
import com.example.kiwdy.model.CursoLocal;
import com.example.kiwdy.ui.compartido.UIDialogs;

import java.util.List;

public class BorradoresFragment extends Fragment {

    private BorradoresViewModel mViewModel;
    private FragmentBorradoresBinding binding;

    public static BorradoresFragment newInstance() {
        return new BorradoresFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(BorradoresViewModel.class);
        binding = FragmentBorradoresBinding.inflate(inflater, container, false);

        mViewModel.getmError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                UIDialogs.error(requireContext(), s);
            }
        });

        mViewModel.getmBorradores().observe(getViewLifecycleOwner(), new Observer<List<CursoLocal>>() {
            @Override
            public void onChanged(List<CursoLocal> cursosLocales) {
                BorradorCursoAdapter adapter = new BorradorCursoAdapter(cursosLocales, requireContext(), inflater);

                GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 2);

                binding.rvBorradores.setLayoutManager(layoutManager);
                binding.rvBorradores.setAdapter(adapter);
            }
        });
        mViewModel.leerBorradores();

        return binding.getRoot();
    }


}