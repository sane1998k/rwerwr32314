package com.automat.manager.interfaces;

import java.util.ArrayList;

import retrofit2.Response;

public interface IBodyRetrofit {
    <T> void success(Response<ArrayList<T>> response);
    <T> void bad(Response<ArrayList<T>> response);
    void failed();

}
