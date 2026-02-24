package com.example.kiwdy.ui.compartido.examenes;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.kiwdy.R;
import com.example.kiwdy.api.dto.response.ExamenResponse;
import com.example.kiwdy.databinding.FragmentExamenesBinding;
import com.example.kiwdy.ui.compartido.UIDialogs;
import com.example.kiwdy.ui.compartido.login.LoginActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.List;

public class ExamenesFragment extends Fragment {

    private ExamenesViewModel mViewModel;
    private FragmentExamenesBinding binding;

    public static ExamenesFragment newInstance() {
        return new ExamenesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(ExamenesViewModel.class);
        binding = FragmentExamenesBinding.inflate(inflater, container, false);

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

        mViewModel.getmExamenes().observe(getViewLifecycleOwner(), new Observer<List<ExamenResponse>>() {
            @Override
            public void onChanged(List<ExamenResponse> examenResponses) {
                ExamenAdapter adapter = new ExamenAdapter(examenResponses, requireContext(), inflater, new ExamenAdapter.OnClickListener() {
                    @Override
                    public void onClickGuardarNota(DialogInterface dialog, View dialogView, Button bt, int idExamen, String nota) {
                        mViewModel.getmMostrarBotonFinalizar().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                            @Override
                            public void onChanged(Boolean aBoolean) {
                                bt.setVisibility(View.GONE);
                                dialog.dismiss();
                            }
                        });

                        EditText etNotaDialog = dialogView.findViewById(R.id.etNotaExamenDialog);

                        mViewModel.getmMensajeNotaDialog().observe(getViewLifecycleOwner(), new Observer<String>() {
                            @Override
                            public void onChanged(String s) {
                                etNotaDialog.setError(s);
                            }
                        });
                        mViewModel.guardarNota(idExamen, nota);
                    }
                });

                GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false);

                binding.rvExamenes.setLayoutManager(layoutManager);
                binding.rvExamenes.setAdapter(adapter);
            }
        });
        binding.btGroupExamenes.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup materialButtonToggleGroup, int i, boolean b) {
                MaterialButton bt = materialButtonToggleGroup.findViewById(i);
                String tag = bt.getTag().toString();
                mViewModel.obtenerExamenes(tag, b);
            }
        });

        mViewModel.obtenerExamenes("proximos", true);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}