package com.example.kiwdy.model;

import android.net.Uri;

public class ArchivoDescargado {
    private Uri uri;
    private String mime;

    public ArchivoDescargado() {
    }

    public ArchivoDescargado(Uri uri, String mime) {
        this.uri = uri;
        this.mime = mime;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }
}
