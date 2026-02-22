package com.example.kiwdy.ui.alumno.cursos;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.kiwdy.R;
import com.example.kiwdy.api.ApiClient;
import com.example.kiwdy.api.dto.response.CursoInscripcionResponse;
import com.example.kiwdy.api.dto.response.CursoResponse;
import com.example.kiwdy.api.dto.response.InscripcionResponse;
import com.example.kiwdy.databinding.FragmentDetalleCursoBinding;

import io.noties.markwon.Markwon;

public class DetalleCursoFragment extends Fragment {

    private DetalleCursoViewModel mViewModel;
    private FragmentDetalleCursoBinding binding;
    private SeccionResumenAdapter adapter;

    public static DetalleCursoFragment newInstance() {
        return new DetalleCursoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(DetalleCursoViewModel.class);
        binding = FragmentDetalleCursoBinding.inflate(inflater, container, false);

        mViewModel.getmRequiereExamen().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.tvRequiereExamenDetalleCurso.setText(s);
            }
        });
        mViewModel.getmMostrarBtVerProgreso().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.btProgresoDetalleCurso.setVisibility(View.VISIBLE);
            }
        });

        mViewModel.getmCurso().observe(getViewLifecycleOwner(), new Observer<CursoResponse>() {
            @Override
            public void onChanged(CursoResponse cursoResponse) {
                Markwon markwon = Markwon.create(requireContext());
                binding.etTituloDetalleCurso.setText(cursoResponse.getTitulo());
                binding.tvPrecioDetalleCurso.setText(cursoResponse.getPrecio() + "");
                markwon.setMarkdown(binding.etDescripcionDetalleCurso, cursoResponse.getDescripcion());

                Glide.with(requireContext())
                        .load(ApiClient.URL_BASE + cursoResponse.getPortadaUrl())
                        .placeholder(R.drawable.fondo)
                        .error(R.drawable.fondo)
                        .into(binding.ivPortadaCursoDetalleCurso);
                adapter = new SeccionResumenAdapter(cursoResponse.getSecciones(), requireContext(), getLayoutInflater(), cursoResponse.isEstaInscripto());
                GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false);
                binding.rvSeccionesDetalleCurso.setLayoutManager(layoutManager);
                binding.rvSeccionesDetalleCurso.setAdapter(adapter);

            }

        });
        mViewModel.getmMostrarBtInscribir().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.btInscribirseDetalleCurso.setVisibility(View.VISIBLE);
            }
        });
        mViewModel.getmOcultarBtInscribir().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.btInscribirseDetalleCurso.setVisibility(View.INVISIBLE);
            }
        });
        mViewModel.getmMostrarBtResumir().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.btVerSeccionesDetalle.setVisibility(View.VISIBLE);
            }
        });
        mViewModel.getmHabilitarOnClickSeccion().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                adapter.setOnClickHabilitado(true);
            }
        });
        mViewModel.getmNavegarASeccion().observe(getViewLifecycleOwner(), new Observer<CursoResponse>() {
            @Override
            public void onChanged(CursoResponse cursoResponse) {
                Bundle bundle = new Bundle();
                bundle.putInt("idCurso", cursoResponse.getIdCurso());
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_alumno).navigate(R.id.detalleSeccionFragment, bundle);
                mViewModel.limpiarMutables();
            }
        });

        mViewModel.getmNavegarAProgreso().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Bundle bundle = new Bundle();
                bundle.putInt("idCurso", integer);
                bundle.putString("Desde", "DetalleCurso");
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_alumno).navigate(R.id.progresoAlumnoFragment, bundle);
                mViewModel.limpiarMutables();
            }
        });

        mViewModel.getmInscripcionSolicitada().observe(getViewLifecycleOwner(), new Observer<InscripcionResponse>() {
            @Override
            public void onChanged(InscripcionResponse inscripcionResponse) {
                binding.tvInfoInscripcionDetalleCurso.setText("Inscripción solicitada, Gestione el pago con el instructor envíando un mail a " + inscripcionResponse.getCurso().getUsuarioInstructor().getEmail());
                binding.tvInfoInscripcionDetalleCurso.setVisibility(View.VISIBLE);
                binding.btInscribirseDetalleCurso.setVisibility(View.INVISIBLE);
            }
        });

        mViewModel.mostrarCurso(getArguments());

        binding.btInscribirseDetalleCurso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.inscribir();
            }
        });
        binding.btVerSeccionesDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.mostrarSecciones();
            }
        });

        binding.btProgresoDetalleCurso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.mostrarProgreso();
            }
        });


        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        mViewModel = null;
    }

}