package com.example.workoutlogger.data;

import androidx.annotation.StringRes;

public class Result<T> {
    private T data;
    private Throwable error;
    @StringRes
    private int errorMessageRes;

    private Result(T data) {
        this.data = data;
    }

    private Result(Throwable error, @StringRes int errorMessageRes) {
        this.error = error;
        this.errorMessageRes = errorMessageRes;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(data);
    }

    public static <T> Result<T> error(@StringRes int errorMessageRes) {
        return new Result<>(null, errorMessageRes);
    }

    public static <T> Result<T> error(Throwable error, @StringRes int errorMessageRes) {
        return new Result<>(error, errorMessageRes);
    }

    public T getData() {
        return data;
    }

    @StringRes
    public int getErrorMessageRes() {
        return errorMessageRes;
    }

    public Throwable getError() {
        return error;
    }

    public boolean isSuccess() {
        return data != null;
    }
}
