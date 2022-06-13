package com.automat.manager.interfaces;

import java.util.ArrayList;

import retrofit2.Response;

public interface ICallBack<T> {
    void good(Response<T> response);
    void bad(Response<T> response);
    void failed();
}
