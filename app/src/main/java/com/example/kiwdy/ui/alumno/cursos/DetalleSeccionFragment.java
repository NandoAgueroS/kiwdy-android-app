package com.example.kiwdy.ui.alumno.cursos;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
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
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.kiwdy.BuildConfig;
import com.example.kiwdy.R;
import com.example.kiwdy.api.dto.response.InscripcionResponse;
import com.example.kiwdy.api.dto.response.SeccionResponse;
import com.example.kiwdy.databinding.FragmentDetalleSeccionBinding;
import com.example.kiwdy.model.ArchivoDescargado;
import com.example.kiwdy.model.CursoFinalizadoMensaje;
import com.example.kiwdy.ui.compartido.UIDialogs;
import com.example.kiwdy.ui.compartido.login.LoginActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import io.noties.markwon.Markwon;

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

                Markwon markwon = Markwon.create(requireContext());
                markwon.setMarkdown(binding.tvContenidoSeccionDetalle,seccionResponse.getContenido());

                binding.btMarcarCompletadaDetalle.setEnabled(true);
                //videoView.start();

                DetalleArchivosSeccionAdapter adapter =  new DetalleArchivosSeccionAdapter(seccionResponse.getMateriales(), requireContext(), inflater, new DetalleArchivosSeccionAdapter.OnClickListener() {
                    @Override
                    public void onClick(int idMaterial, String nombreMaterial) {
                        mViewModel.descargarArchivo(idMaterial, nombreMaterial);
                    }
                });
                GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false);
                binding.rvMaterialesExtraDetalle.setLayoutManager(layoutManager);
                binding.rvMaterialesExtraDetalle.setAdapter(adapter);

            }
        });
        mViewModel.getmMostrarVideo().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                VideoView videoView = binding.vvSeccionDetalle;
                binding.flVideoSeccionDetalle.setVisibility(View.VISIBLE);
                videoView.setVideoPath(BuildConfig.URL_BASE_API + s);
                MediaController mediaController = new MediaController(requireContext());
                mediaController.setAnchorView(videoView);
                videoView.setMediaController(mediaController);
                videoView.start();
            }
        });

        mViewModel.getmMostrarBotonMarcarCompletada().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.btMarcarCompletadaDetalle.setEnabled(aBoolean);
            }
        });
        mViewModel.getmAbrirArchivoDescargado().observe(getViewLifecycleOwner(), new Observer<ArchivoDescargado>() {
            @Override
            public void onChanged(ArchivoDescargado archivoDescargado) {
                UIDialogs.archivoDescargadoDialog(requireContext(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(archivoDescargado.getUri(), archivoDescargado.getMime());
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivity(intent);
                        }
                    });
            }
        });
        mViewModel.getmArchivoDescargado().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                UIDialogs.archivoDescargadoLegacy(requireContext());
            }
        });

        mViewModel.getmSeccionesFinalizadas().observe(getViewLifecycleOwner(), new Observer<CursoFinalizadoMensaje>() {
            @Override
            public void onChanged(CursoFinalizadoMensaje cursoFinalizadoMensaje) {
                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Fin del curso")
                        .setMessage(cursoFinalizadoMensaje.getMensaje())
                        .setPositiveButton("Ver detalles", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Bundle bundle = new Bundle();
                                bundle.putInt("idCurso", cursoFinalizadoMensaje.getIdCurso());
                                bundle.putString("Desde", "DetalleSeccion");
                                Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_alumno).navigate(R.id.progresoAlumnoFragment, bundle);
                                mViewModel.limpiarMutables();
                            }
                        })
                        .setNegativeButton("Cerrar", null)
                        .show();
            }
        });

        mViewModel.getmOcultarVideo().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.flVideoSeccionDetalle.setVisibility(View.GONE);
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