package com.automat.manager.interfaces;

import androidx.annotation.NonNull;

import com.automat.manager.enums.EErrorCode;

import retrofit2.Response;

public interface IRetrofitNotArray {
    <T> void success(Response<T> response);
    <T> void bad(Response<T> response);
    <T> void failed(@NonNull retrofit2.Call<T> call, @NonNull Throwable t);
}
