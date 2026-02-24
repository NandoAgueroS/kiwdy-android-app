package com.example.kiwdy.ui.instructor.crear_curso;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kiwdy.R;
import com.example.kiwdy.api.ApiClient;
import com.example.kiwdy.databinding.FragmentCrearCursoBinding;
import com.example.kiwdy.model.CursoLocal;
import com.example.kiwdy.ui.compartido.UIDialogs;
import com.example.kiwdy.ui.instructor.InstructorMainActivity;
import com.google.android.material.materialswitch.MaterialSwitch;

import io.noties.markwon.Markwon;
import io.noties.markwon.editor.MarkwonEditor;
import io.noties.markwon.editor.MarkwonEditorTextWatcher;

public class CrearCursoFragment extends Fragment {

    private CrearCursoViewModel mViewModel;
    private FragmentCrearCursoBinding binding;
    private ActivityResultLauncher<Intent> arl;
    private Intent intent;
    private Markwon markwon;
    private MarkwonEditor editor;
    private String descripcionTextoPlano;


    public static CrearCursoFragment newInstance() {
        return new CrearCursoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(CrearCursoViewModel.class);
        binding = FragmentCrearCursoBinding.inflate(inflater, container, false);

        markwon = Markwon.create(requireContext());
        editor = MarkwonEditor.create(markwon);

        binding.etDescripcion.addTextChangedListener(MarkwonEditorTextWatcher.withProcess(editor));

        EditText etTitulo= binding.etTitulo;
        EditText etDescripcion = binding.etDescripcion;
        EditText etPrecio = binding.etPrecio;
        EditText etNotaAprobacion = binding.etNotaAprobacion;
        TextView tvError = binding.tvErrorCrearCurso;
        CheckBox cbRequiereExamen = binding.cbRequiereExamen;
        ImageView ivCurso = binding.ivCurso;
        MaterialSwitch stVistaPreviaDescripcion = binding.stVistaPreviaDescripcionCurso;
        Button btAgregarSeccion = binding.btAgregarSeccion;
        Button btGuardarCurso = binding.btGuardarCurso;
        RecyclerView rvSecciones = binding.rvSecciones;

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
        ivCurso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arl.launch(intent);
            }
        });
        mViewModel.getmImagenUri().observe(getViewLifecycleOwner(), new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                ivCurso.setImageURI(uri);
                ivCurso.setVisibility(View.VISIBLE);
            }
        });

        btGuardarCurso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.guardarCurso(
                        etTitulo.getText().toString(),
                        descripcionTextoPlano,
                        etDescripcion.getText().toString(),
                        etPrecio.getText().toString(),
                        etNotaAprobacion.getText().toString(),
                        cbRequiereExamen.isChecked(),
                        stVistaPreviaDescripcion.isChecked()
                );
            }
        });

        mViewModel.getmCursoLocal().observe(getViewLifecycleOwner(), new Observer<CursoLocal>() {
            @Override
            public void onChanged(CursoLocal cursoLocal) {

                etTitulo.setText(cursoLocal.getTitulo());
                etDescripcion.setText(cursoLocal.getDescripcion());
                descripcionTextoPlano = cursoLocal.getDescripcion();
                etPrecio.setText(cursoLocal.getPrecio() + "");
                RecyclerView.Adapter adapter = new SeccionResumenAdapter(cursoLocal.getSeccionLocalList(), getContext(), inflater, new SeccionResumenAdapter.OnClickListener() {
                    @Override
                    public void onClick(int orden) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("orden", orden);
                        bundle.putString("nombreArchivoBorrador", cursoLocal.getNombreArchivoBorrador());
                        Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main).navigate(R.id.crearSeccionFragment, bundle);
                    }
                });
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1,GridLayoutManager.VERTICAL,false);
                rvSecciones.setLayoutManager(gridLayoutManager);
                rvSecciones.setAdapter(adapter);
            }
        });

        mViewModel.getmCursoLocalModoVisualizacion().observe(getViewLifecycleOwner(), new Observer<CursoLocal>() {
            @Override
            public void onChanged(CursoLocal cursoLocal) {
                //etTitulo.setEnabled(false);
                //etDescripcion.setEnabled(false);
                etTitulo.setFocusable(false);
                etTitulo.setFocusableInTouchMode(false);
                etDescripcion.setFocusable(false);
                etDescripcion.setFocusableInTouchMode(false);
                etPrecio.setFocusable(false);
                etPrecio.setFocusableInTouchMode(false);
                etNotaAprobacion.setFocusable(false);
                etNotaAprobacion.setFocusableInTouchMode(false);
                ivCurso.setClickable(false);
                stVistaPreviaDescripcion.setChecked(true);
                stVistaPreviaDescripcion.setEnabled(false);
                cbRequiereExamen.setEnabled(false);
                btGuardarCurso.setVisibility(View.INVISIBLE);
                btAgregarSeccion.setVisibility(View.INVISIBLE);

                Glide.with(requireContext())
                        .load(ApiClient.URL_BASE + cursoLocal.getPortadaUrl())
                        .placeholder(R.drawable.fondo)
                        .error(R.drawable.fondo)
                        .into(binding.ivCurso);

                etTitulo.setText(cursoLocal.getTitulo());
                etDescripcion.setText(cursoLocal.getDescripcion());
                etPrecio.setText(cursoLocal.getPrecio() + "");
                RecyclerView.Adapter adapter = new SeccionResumenAdapter(cursoLocal.getSeccionLocalList(), getContext(), inflater, new SeccionResumenAdapter.OnClickListener() {
                    @Override
                    public void onClick(int orden) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("orden", orden);
                        bundle.putInt("idCurso", cursoLocal.getIdCurso());
                        Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main).navigate(R.id.crearSeccionFragment, bundle);
                    }
                });
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1,GridLayoutManager.VERTICAL,false);
                rvSecciones.setLayoutManager(gridLayoutManager);
                rvSecciones.setAdapter(adapter);
            }
        });

        mViewModel.getmNavegarACrearSeccion().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("nombreArchivoBorrador", s);
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main).navigate(R.id.crearSeccionFragment, bundle);
                mViewModel.limpiarMutables();
            }
        });

        btAgregarSeccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.guardarProgresoCurso(
                        etTitulo.getText().toString(),
                        descripcionTextoPlano,
                        etDescripcion.getText().toString(),
                        etPrecio.getText().toString(),
                        etNotaAprobacion.getText().toString(),
                        cbRequiereExamen.isChecked(),
                        stVistaPreviaDescripcion.isChecked(),
                        true,
                -1
                );
            }
        });
        mViewModel.getmMostrarNotaInput().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                etNotaAprobacion.setVisibility(View.VISIBLE);
                etNotaAprobacion.setText(String.valueOf(aDouble));
            }
        });

        mViewModel.getmOcultarNotaInput().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                etNotaAprobacion.setVisibility(View.GONE);
                etNotaAprobacion.setText(String.valueOf(aDouble));
            }
        });

        mViewModel.getmMostrarVistaPrevia().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                descripcionTextoPlano = etDescripcion.getText().toString();
                markwon.setMarkdown(etDescripcion, etDescripcion.getText().toString());
                etDescripcion.setFocusable(false);
                etDescripcion.setFocusableInTouchMode(false);
                etDescripcion.setClickable(true);
            }
        });

        mViewModel.getmOcultarVistaPrevia().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                etDescripcion.setText(descripcionTextoPlano);
                etDescripcion.setFocusable(true);
                etDescripcion.setFocusableInTouchMode(true);
            }
        });

        mViewModel.getmActivarCheckRequiereExamen().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                cbRequiereExamen.setChecked(true);
            }
        });

        cbRequiereExamen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton buttonView, boolean isChecked) {
                mViewModel.mostrarNotaAprobacionInput(isChecked);
            }
        });
        stVistaPreviaDescripcion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton buttonView, boolean isChecked) {
                mViewModel.alternarVistaPreviaDescripcion(isChecked);
            }
        });
        //mViewModel.restaurarCurso(getArguments());
        mViewModel.cargarBorrador(getArguments());

        etDescripcion.setFocusable(true);
        etDescripcion.setFocusableInTouchMode(true);

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

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        mViewModel.cargarBorrador(getArguments());
    }
}