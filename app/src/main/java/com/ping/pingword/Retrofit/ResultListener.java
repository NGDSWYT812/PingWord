package com.ping.pingword.Retrofit;


/**
 * Created by lhy on 2016/3/30.
 */
public interface ResultListener<T> {
    void onSucess(T... result);
    void onFailre(T... error);
    void onServerError(String message);
}
