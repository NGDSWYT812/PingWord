package com.ping.pingword.task;

public interface BackgroundWork<T> {
    T doInBackground() throws Exception;
}
