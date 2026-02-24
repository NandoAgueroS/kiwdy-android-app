package com.example.kiwdy.ui.instructor.examenes;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kiwdy.R;
import com.example.kiwdy.api.dto.response.ExamenResponse;
import com.example.kiwdy.api.dto.response.InscripcionResponse;
import com.example.kiwdy.api.dto.response.UsuarioResponse;
import com.example.kiwdy.databinding.FragmentAgendarExamenBinding;
import com.example.kiwdy.ui.compartido.UIDialogs;
import com.example.kiwdy.ui.compartido.login.LoginActivity;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;

public class AgendarExamenFragment extends Fragment {

    private AgendarExamenViewModel mViewModel;
    private FragmentAgendarExamenBinding binding;

    public static AgendarExamenFragment newInstance() {
        return new AgendarExamenFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(AgendarExamenViewModel.class);
        binding = FragmentAgendarExamenBinding.inflate(inflater, container, false);

        EditText etFechaExamen = binding.etFechaAgendarExamen;
        EditText etHoraExamen = binding.etHoraAgendarExamen;
        EditText etLinkODireccionExamen = binding.etLinkODireccionAgendarExamen;
        TextView tvFechaInscripto = binding.tvFechaInscriptoAgendarExamen;
        TextView tvNombreApellidoAlumno = binding.tvNombreApellidoAlumnoAgendarExamen;
        TextView tvError = binding.tvErrorAgendarExamen;
        MaterialButtonToggleGroup btGroupModalidad = binding.btGroupModalidad;
        Button btAgendarExamen = binding.btAgendarExamen;

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

        mViewModel.getmInscripcion().observe(getViewLifecycleOwner(), new Observer<InscripcionResponse>() {
            @Override
            public void onChanged(InscripcionResponse inscripcionResponse) {
                btAgendarExamen.setEnabled(true);
                UsuarioResponse alumno = inscripcionResponse.getUsuarioAlumno();
                tvNombreApellidoAlumno.setText(alumno.getNombre() + " " + alumno.getApellido());
                tvFechaInscripto.setText(inscripcionResponse.getFechaInicio().toLocalDate().toString());
            }
        });

        mViewModel.getmExamenAgendado().observe(getViewLifecycleOwner(), new Observer<ExamenResponse>() {
            @Override
            public void onChanged(ExamenResponse examenResponse) {
                Toast.makeText(requireContext(), "Se agendó el exámen", Toast.LENGTH_LONG).show();
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main).navigateUp();
            }
        });

        mViewModel.recuperarInscripcion(getArguments());

        btAgendarExamen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.agendar(
                        getActivity().findViewById(btGroupModalidad.getCheckedButtonId()).getTag().toString(),
                        etFechaExamen.getText().toString(),
                        etHoraExamen.getText().toString(),
                        etLinkODireccionExamen.getText().toString()
                );
            }
        });

        etFechaExamen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();

                builder.setTitleText("Selecciona la fecha del exámen");

                MaterialDatePicker<Long> datePicker = builder.build();

                datePicker.show(getActivity().getSupportFragmentManager(), "DATE_PICKER");

                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long aLong) {
                        Instant instant = Instant.ofEpochMilli(aLong);

                        LocalDate fecha = instant.atZone(ZoneId.of("UTC")).toLocalDate();

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                        etFechaExamen.setText(fecha.format(formatter));
                        etFechaExamen.clearFocus();
                    }
                });
            }
        }
        );
                MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setHour(12)
                        .setMinute(0)
                        .setTitleText("Selecciona la hora del exámen")
                        .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                        .build();
        etHoraExamen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker.show(getChildFragmentManager(), "TIME_PICKER");
            }
        });

        timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hora = timePicker.getHour();
                int minutos = timePicker.getMinute();

                LocalTime tiempo = LocalTime.of(hora, minutos);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

                etHoraExamen.setText(tiempo.format(formatter));
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}