package com.example.kiwdy.ui.compartido;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.example.kiwdy.model.ArchivoDescargado;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;


public final class UIDialogs {

    public static void error(Context context, String mensaje){
        new MaterialAlertDialogBuilder(context)
                .setTitle("Error")
                .setMessage(mensaje)
                .setPositiveButton("Ok", null)
                .show();
    }

    public static void errorEnRequest(Context context, int codigo){
        String mensaje = "";

        switch (codigo){
            case 401: mensaje = "Sessión inválida";
            break;
            case 404: mensaje = "No se encontró el recurso";
            break;
            case 500: mensaje = "Error en el servidor";
            break;
            default: mensaje = "Error inesperado";
        }

        new MaterialAlertDialogBuilder(context)
                .setTitle("Error")
                .setMessage(mensaje)
                .setPositiveButton("Ok", null)
                .show();
    }

    public static void validacion(Context context, String mensaje){
        new MaterialAlertDialogBuilder(context)
                .setTitle("Datos inválidos")
                .setMessage(mensaje)
                .setPositiveButton("Ok", null)
                .show();
    }
    public static void archivoDescargadoDialog(Context context, DialogInterface.OnClickListener positiveListener){
        new MaterialAlertDialogBuilder(context)
                .setTitle("Descarga completada")
                .setMessage("Archivo descargado correctamente")
                .setNegativeButton("Cerrar", null)
                .setPositiveButton("Abrir", positiveListener)
                .show();
    }
    public static void archivoDescargadoLegacy(Context context) {
        new MaterialAlertDialogBuilder(context)
                .setTitle("Descarga completada")
                .setMessage("Archivo descargado correctamente")
                .setPositiveButton("Ok", null)
                .show();
    }
}
