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

import com.example.kiwdy.R;
import com.example.kiwdy.databinding.FragmentCrearCursoBinding;
import com.example.kiwdy.model.CursoLocal;
import com.example.kiwdy.ui.instructor.InstructorMainActivity;

public class CrearCursoFragment extends Fragment {

    private CrearCursoViewModel mViewModel;
    private FragmentCrearCursoBinding binding;
    private ActivityResultLauncher<Intent> arl;
    private Intent intent;


    public static CrearCursoFragment newInstance() {
        return new CrearCursoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(requireActivity()).get(CrearCursoViewModel.class);
        binding = FragmentCrearCursoBinding.inflate(inflater, container, false);

        abrirGaleria();
        binding.btAgregarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arl.launch(intent);
            }
        });
        mViewModel.getmImagenUri().observe(getViewLifecycleOwner(), new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                binding.ivCurso.setImageURI(uri);
                binding.ivCurso.setVisibility(View.VISIBLE);
            }
        });

        binding.btGuardarCurso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.guardarCurso(
                        binding.etTitulo.getText().toString(),
                        binding.etDescripcion.getText().toString()
                );
            }
        });

        mViewModel.getmCursoLocal().observe(getViewLifecycleOwner(), new Observer<CursoLocal>() {
            @Override
            public void onChanged(CursoLocal cursoLocal) {

                binding.etTitulo.setText(cursoLocal.getTitulo());
                binding.etDescripcion.setText(cursoLocal.getDescripcion());
                RecyclerView.Adapter adapter = new SeccionResumenAdapter(cursoLocal.getSeccionLocalList(), getContext(), inflater);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1,GridLayoutManager.VERTICAL,false);
                binding.rvSecciones.setLayoutManager(gridLayoutManager);
                binding.rvSecciones.setAdapter(adapter);
            }
        });

        binding.btAgregarSeccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.guardarProgresoCurso(
                        binding.etTitulo.getText().toString(),
                        binding.etDescripcion.getText().toString()
                );
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main).navigate(R.id.crearSeccionFragment);
            }
        });
        return binding.getRoot();
    }

    private void abrirGaleria() {
        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        arl = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                //Log.d("AgregarInmuebleFragment", "Result: " + result);
                mViewModel.recibirImagen(result);

            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        //InstructorMainActivity.seccionesLocal.clear();
    }
}