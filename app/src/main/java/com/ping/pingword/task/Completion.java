package com.ping.pingword.task;

public interface Completion<T> {
    void onSuccess(T result);
    void onError(Exception e);
}
