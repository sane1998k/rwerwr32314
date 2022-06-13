package com.automat.manager.interfaces;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import retrofit2.Response;

public interface IRetrofitArray {
    <T> void success(Response<ArrayList<T>> response);
    <T> void bad(Response<ArrayList<T>> response);
    <T> void failed(@NonNull retrofit2.Call<ArrayList<T>> call, @NonNull Throwable t);
}
