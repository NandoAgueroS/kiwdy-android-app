package com.example.kiwdy.ui.compartido.inicio;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kiwdy.R;
import com.example.kiwdy.api.dto.response.CursoResponse;
import com.example.kiwdy.api.dto.response.UsuarioResponse;
import com.example.kiwdy.databinding.FragmentInicioBinding;
import com.example.kiwdy.ui.compartido.UIDialogs;
import com.example.kiwdy.ui.compartido.login.LoginActivity;

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

        binding.btCrearCurso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main).navigate(R.id.crearCursoFragment);
            }
        });

        mViewModel.getmError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                UIDialogs.error(requireContext(), s);
            }
        });

        mViewModel.getmPerfilUsuario().observe(getViewLifecycleOwner(), new Observer<UsuarioResponse>() {
            @Override
            public void onChanged(UsuarioResponse usuarioResponse) {
                TextView tvNombreNavHeader = getActivity().findViewById(R.id.tvNombreNavHeader);
                TextView tvEmailNavHeader = getActivity().findViewById(R.id.tvEmailNavHeader);
                tvNombreNavHeader.setText(usuarioResponse.getNombre() + " " + usuarioResponse.getApellido());
                tvEmailNavHeader.setText(usuarioResponse.getEmail());
            }
        });
        mViewModel.getmVistaInstructor().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.btCrearCurso.setVisibility(View.VISIBLE);
                binding.tvDescripcionVistaInicio.setText("Crea un curso o navega por el menú para ver más opciones");
                binding.tvTituloListaInicio.setText("Mis cursos populares");
            }
        });
        mViewModel.getmVistaAlumno().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.btCrearCurso.setVisibility(View.GONE);
                binding.tvDescripcionVistaInicio.setText("Inscribete a un curso o navega por el menú para ver más opciones");
                binding.tvTituloListaInicio.setText("Cursos populares");
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

        mViewModel.verificarRol();
        mViewModel.cargarListaCursos();
        mViewModel.recuperarPerfil();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

}