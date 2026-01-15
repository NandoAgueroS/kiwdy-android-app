package com.example.kiwdy.ui.compartido.registro;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.kiwdy.R;
import com.example.kiwdy.api.dto.response.UsuarioResponse;
import com.example.kiwdy.databinding.ActivityRegistroBinding;
import com.example.kiwdy.ui.compartido.login.LoginActivity;

public class RegistroActivity extends AppCompatActivity {

    private ActivityRegistroBinding binding;
    private RegistroActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(RegistroActivityViewModel.class);
        binding = ActivityRegistroBinding.inflate(getLayoutInflater());

        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.registro), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel.getmUsuarioCreado().observe(this, new Observer<UsuarioResponse>() {
            @Override
            public void onChanged(UsuarioResponse usuarioResponse) {
                new AlertDialog.Builder(RegistroActivity.this)
                        .setTitle("Usuario registrado con éxito")
                        .setMessage("Inicie sesión para continuar")
                        .setPositiveButton("Iniciar sesión", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getApplication(), LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        })
                        .show();

            }
        });

        binding.btRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.registrarUsuario(
                        findViewById(binding.btGroupRolRegistro.getCheckedButtonId()).getTag().toString(),
                        binding.etNombreRegistro.getText().toString(),
                        binding.etApellidoRegistro.getText().toString(),
                        binding.etEmailRegistro.getText().toString(),
                        binding.etTelefonoRegistro.getText().toString(),
                        binding.etContraseniaRegistro.getText().toString(),
                        binding.etConfirmarContraseARegistro.getText().toString());
            }
        });

    }
}