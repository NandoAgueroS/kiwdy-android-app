package com.example.kiwdy.api.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {

    private static SharedPreferences getSPMPrivate(Context context){
        return context.getSharedPreferences("preferencias.xml", Context.MODE_PRIVATE);
    }

    public static void guardarToken(Context context, String token){
        SharedPreferences.Editor editor = getSPMPrivate(context).edit();

        editor.putString("token","Bearer " + token);
        editor.apply();
    }

    public static void eliminarToken(Context context){
        SharedPreferences.Editor editor= getSPMPrivate(context).edit();

        editor.remove("token");
        editor.commit();
    }

    public static String leerToken(Context context){
        return getSPMPrivate(context).getString("token",null);
    }

    public static void guardarEmail(Context context, String email){
        SharedPreferences.Editor editor = getSPMPrivate(context).edit();

        editor.putString("email", email);
        editor.apply();
    }

    public static String leerEmail(Context context){
        return getSPMPrivate(context).getString("email",null);
    }
}
