package com.example.workoutlogger.data;

public class Result<T> {
    private T data;
    private Throwable error;

    public Result(T data) {
        this.data = data;
    }

    public Result(Throwable error) {
        this.error = error;
    }

    public T getData() {
        return data;
    }

    public Throwable getError() {
        return error;
    }

    public boolean isSuccess() {
        return data != null;
    }
}
