package com.example.kiwdy.ui.instructor.crear_curso;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.kiwdy.BuildConfig;
import com.example.kiwdy.R;
import com.example.kiwdy.databinding.FragmentCrearSeccionBinding;
import com.example.kiwdy.model.MaterialExtra;
import com.example.kiwdy.model.SeccionLocal;
import com.example.kiwdy.ui.compartido.UIDialogs;
import com.google.android.material.materialswitch.MaterialSwitch;

import java.util.List;

import io.noties.markwon.Markwon;
import io.noties.markwon.editor.MarkwonEditor;
import io.noties.markwon.editor.MarkwonEditorTextWatcher;

public class CrearSeccionFragment extends Fragment {

    private CrearSeccionViewModel mViewModel;
    private FragmentCrearSeccionBinding binding;
    private ActivityResultLauncher<Intent> arlVideo;
    private ActivityResultLauncher<Intent> arlArchivo;
    private Intent intentVideo;
    private Intent intentArchivo;
    private Markwon markwon;
    private MarkwonEditor editor;
    private String contenidoSeccionTextoPlano;


    public static CrearSeccionFragment newInstance() {
        return new CrearSeccionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCrearSeccionBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(CrearSeccionViewModel.class);

        markwon = Markwon.create(requireContext());
        editor = MarkwonEditor.create(markwon);

        binding.etContenidoSeccion.addTextChangedListener(MarkwonEditorTextWatcher.withProcess(editor));

        EditText etTituloSeccion = binding.etTituloSeccion;
        EditText etContenidoSeccion = binding.etContenidoSeccion;
        TextView tvError = binding.tvErrorCrearSeccion;
        Button btGuardarSeccion = binding.btGuardarSeccion;
        Button btAgregarArchivo = binding.btAgregarArchivo;
        Button btAgregarVideo = binding.btAgregarVideo;
        VideoView vvSeccion = binding.vvSeccion;
        FrameLayout flVideoSeccion = binding.flVideoSeccion;
        RecyclerView rvMaterialesExtra = binding.rvMaterialesExtra;
        MaterialSwitch stVistaPreviaContenido = binding.stVistaPreviaContenidoSeccion;

        mViewModel.getmError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                UIDialogs.error(requireContext(), s);
            }
        });
        mViewModel.getmErrorDeValidacion().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                UIDialogs.validacion(requireContext(), s);
            }
        });
        mViewModel.getmMensaje().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tvError.setText(s);
            }
        });

        abrirGaleria();
        abrirArchivos();

        btAgregarVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arlVideo.launch(intentVideo);
            }
        });

        btAgregarArchivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arlArchivo.launch(intentArchivo);
            }
        });

        mViewModel.getmVideoUri().observe(getViewLifecycleOwner(), new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                MediaController mediaController = new MediaController(requireContext());
                mediaController.setAnchorView(vvSeccion);
                vvSeccion.setMediaController(mediaController);
                vvSeccion.setVideoURI(uri);
                flVideoSeccion.setVisibility(View.VISIBLE);
                vvSeccion.start();
                btAgregarVideo.setText("Cambiar video");
            }
        });
        mViewModel.getmMaterialesExtra().observe(getViewLifecycleOwner(), new Observer<List<MaterialExtra>>() {
            @Override
            public void onChanged(List<MaterialExtra> materialesExtra) {
                MaterialExtraAdapter adapter = new MaterialExtraAdapter(materialesExtra, requireContext(), getLayoutInflater());
                GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false);
                rvMaterialesExtra.setLayoutManager(gridLayoutManager);
                rvMaterialesExtra.setAdapter(adapter);
            }
        });
        mViewModel.getmSeccionAgregada().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Bundle bundle = new Bundle();

                Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main).navigateUp();
                mViewModel.limpiarMutables();
            }
        });
        mViewModel.getmSeccionLocal().observe(getViewLifecycleOwner(), new Observer<SeccionLocal>() {
            @Override
            public void onChanged(SeccionLocal seccionLocal) {
                etContenidoSeccion.setText(seccionLocal.getContenido());
                contenidoSeccionTextoPlano = seccionLocal.getContenido();
                etTituloSeccion.setText(seccionLocal.getTitulo());
            }
        });
        mViewModel.getmSeccionLocalModoVisualizacion().observe(getViewLifecycleOwner(), new Observer<SeccionLocal>() {
            @Override
            public void onChanged(SeccionLocal seccionLocal) {
                etContenidoSeccion.setFocusable(false);
                etContenidoSeccion.setFocusableInTouchMode(false);
                etTituloSeccion.setFocusable(false);
                etTituloSeccion.setFocusableInTouchMode(false);
                btAgregarArchivo.setVisibility(View.INVISIBLE);
                btAgregarVideo.setVisibility(View.INVISIBLE);
                btGuardarSeccion.setVisibility(View.INVISIBLE);

                stVistaPreviaContenido.setChecked(true);
                stVistaPreviaContenido.setEnabled(false);
                etContenidoSeccion.setText(seccionLocal.getContenido());
                etTituloSeccion.setText(seccionLocal.getTitulo());
                MaterialExtraAdapter adapter = new MaterialExtraAdapter(seccionLocal.getMaterialesExtra(), requireContext(), getLayoutInflater());
                GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false);
                rvMaterialesExtra.setLayoutManager(gridLayoutManager);
                rvMaterialesExtra.setAdapter(adapter);
            }
        });

        mViewModel.getmVideoUrlPath().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                MediaController mediaController = new MediaController(requireContext());
                mediaController.setAnchorView(vvSeccion);
                vvSeccion.setMediaController(mediaController);
                vvSeccion.setVideoPath(BuildConfig.URL_BASE_API + s);
                flVideoSeccion.setVisibility(View.VISIBLE);
                vvSeccion.start();
            }
        });

        mViewModel.getmMostrarVistaPrevia().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                contenidoSeccionTextoPlano = etContenidoSeccion.getText().toString();
                markwon.setMarkdown(etContenidoSeccion, etContenidoSeccion.getText().toString());
                etContenidoSeccion.setFocusable(false);
                etContenidoSeccion.setFocusableInTouchMode(false);
                etContenidoSeccion.setClickable(true);
            }
        });
        mViewModel.getmOcultarVistaPrevia().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                etContenidoSeccion.setText(contenidoSeccionTextoPlano);
                etContenidoSeccion.setFocusable(true);
                etContenidoSeccion.setFocusableInTouchMode(true);
            }
        });

        stVistaPreviaContenido.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton buttonView, boolean isChecked) {
                mViewModel.alternarVistaPreviaDescripcion(isChecked);
            }
        });


        btGuardarSeccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.guardarProgresoSeccion(
                        etTituloSeccion.getText().toString(),
                        contenidoSeccionTextoPlano,
                        etContenidoSeccion.getText().toString(),
                        stVistaPreviaContenido.isChecked()
                );
            }
        });

        mViewModel.recuperarCurso(getArguments());

        etContenidoSeccion.setFocusable(true);
        etContenidoSeccion.setFocusableInTouchMode(true);
        return binding.getRoot();
    }
    private void abrirGaleria() {
        intentVideo = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        arlVideo = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                //Log.d("AgregarInmuebleFragment", "Result: " + result);
                mViewModel.recibirVideo(result);

            }
        });
    }
    private void abrirArchivos() {
        intentArchivo = new Intent(Intent.ACTION_GET_CONTENT);
        intentArchivo.setType("*/*");
        intentArchivo.addCategory(Intent.CATEGORY_OPENABLE);
        arlArchivo = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                //Log.d("AgregarInmuebleFragment", "Result: " + result);
                mViewModel.recibirArchivo(result);

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        mViewModel.limpiarMutables();
    }
}