package com.example.kiwdy.api;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    private static final DateTimeFormatter formatter =  DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    @Override
    public void write(JsonWriter out, LocalDateTime value) throws IOException {
        if (value == null){
            out.nullValue();
        } else {
            out.value(value.format(formatter));
        }

    }

    @Override
    public LocalDateTime read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL){
            in.nextNull();
            return null;
        }
        String dateString = in.nextString();
        if (dateString == null || dateString.isEmpty()){
            return null;
        }
        try {
            return OffsetDateTime.parse(dateString).toLocalDateTime();
        } catch (Exception e) {
            return LocalDateTime.parse(dateString, formatter);
        }
    }
}
