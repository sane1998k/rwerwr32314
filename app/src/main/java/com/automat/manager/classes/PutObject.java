package com.automat.manager.classes;

import android.content.SharedPreferences;

import com.google.gson.Gson;

public class PutObject {
    public static  <T> void save(SharedPreferences sharedPreferences, T t, String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(t);
        editor.putString(key, json);
        editor.commit();
    }

    public static <T> T retrieve(SharedPreferences sharedPreferences, String key, Class<? extends T> c) {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key, "");
        T obj = gson.fromJson(json, c);
        return obj;
    }
}
