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

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.kiwdy.R;
import com.example.kiwdy.databinding.FragmentCrearSeccionBinding;
import com.example.kiwdy.model.MaterialExtra;

import java.util.List;

import io.noties.markwon.Markwon;
import io.noties.markwon.editor.MarkwonEditor;
import io.noties.markwon.editor.MarkwonEditorTextWatcher;

public class CrearSeccionFragment extends Fragment {

    private CrearCursoViewModel mViewModel;
    private FragmentCrearSeccionBinding binding;
    private ActivityResultLauncher<Intent> arlVideo;
    private ActivityResultLauncher<Intent> arlArchivo;
    private Intent intentVideo;
    private Intent intentArchivo;
    private Markwon markwon;
    private MarkwonEditor editor;


    public static CrearSeccionFragment newInstance() {
        return new CrearSeccionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCrearSeccionBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(requireActivity()).get(CrearCursoViewModel.class);

        markwon = Markwon.create(requireContext());
        editor = MarkwonEditor.create(markwon);

        binding.etContenidoSeccion.addTextChangedListener(MarkwonEditorTextWatcher.withProcess(editor));

        abrirGaleria();
        abrirArchivos();

        binding.btAgregarVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arlVideo.launch(intentVideo);
            }
        });

        binding.btAgregarArchivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arlArchivo.launch(intentArchivo);
            }
        });

        mViewModel.getmVideoUri().observe(getViewLifecycleOwner(), new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                VideoView videoView = binding.vvSeccion;
                MediaController mediaController = new MediaController(requireContext());
                mediaController.setAnchorView(videoView);
                videoView.setMediaController(mediaController);
                videoView.setVideoURI(uri);
                binding.flVideoSeccion.setVisibility(View.VISIBLE);
                videoView.start();
                binding.btAgregarVideo.setText("Cambiar video");
            }
        });
        mViewModel.getmMaterialesExtra().observe(getViewLifecycleOwner(), new Observer<List<MaterialExtra>>() {
            @Override
            public void onChanged(List<MaterialExtra> materialesExtra) {
                MaterialExtraAdapter adapter = new MaterialExtraAdapter(materialesExtra, requireContext(), getLayoutInflater());
                GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false);
                binding.rvMaterialesExtra.setLayoutManager(gridLayoutManager);
                binding.rvMaterialesExtra.setAdapter(adapter);
            }
        });
        mViewModel.getmSeccionAgregada().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main).navigateUp();
            }
        });
        binding.btGuardarSeccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.guardarProgresoSeccion(
                        binding.etTituloSeccion.getText().toString(),
                        binding.etContenidoSeccion.getText().toString()
                );
            }
        });
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