package com.example.kiwdy.ui.compartido.login;

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

import com.example.kiwdy.ui.instructor.InstructorMainActivity;
import com.example.kiwdy.R;
import com.example.kiwdy.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private LoginActivityViewModel mv;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mv = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(LoginActivityViewModel.class);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        EdgeToEdge.enable(this);

        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mv.getmLoginInstructor().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                mv.guardarEmail(binding.etEmail.getText().toString());
                Intent intent = new Intent(LoginActivity.this, InstructorMainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        mv.getmMensaje().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.spinKit.setVisibility(View.GONE);
                binding.tvError.setText(s);
            }
        });

        binding.btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tvError.setText("");
                binding.spinKit.setVisibility(View.VISIBLE);
                String email = binding.etEmail.getText().toString();
                String clave = binding.etClave.getText().toString();
                int rol = Integer.parseInt(findViewById(binding.btGroupRol.getCheckedButtonId()).getTag().toString());
                mv.login(email, clave, rol);
            }
        });
        mv.getmEmailGuardado().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.etEmail.setText(s);
            }
        });
        mv.getmSesionInvalida().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.tvError.setText("Su sesión expiró, ingrese nuevamente");
            }
        });
        mv.verificarSesionExpirada(getIntent());
        mv.recuperarEmail();

    }
}