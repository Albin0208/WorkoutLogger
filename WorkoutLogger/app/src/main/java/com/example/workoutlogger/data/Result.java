package com.example.workoutlogger.data;

import androidx.annotation.StringRes;

/**
 * A generic class representing the result of an operation that can be either successful or contain an error.
 *
 * @param <T> The type of data that can be contained in the result.
 */
public class Result<T> {

    private T data;
    private Throwable error;
    @StringRes
    private int errorMessageRes;

    /**
     * Private constructor for creating a successful result.
     *
     * @param data The data to be contained in the result.
     */
    private Result(T data) {
        this.data = data;
    }

    /**
     * Private constructor for creating an error result with a resource ID for the error message.
     *
     * @param error           The error that occurred.
     * @param errorMessageRes The resource ID of the error message.
     */
    private Result(Throwable error, @StringRes int errorMessageRes) {
        this.error = error;
        this.errorMessageRes = errorMessageRes;
    }

    /**
     * Factory method to create a successful result.
     *
     * @param <T>  The type of data.
     * @param data The data to be contained in the result.
     * @return A successful result with the provided data.
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(data);
    }

    /**
     * Factory method to create an error result with a resource ID for the error message.
     *
     * @param <T>              The type of data.
     * @param errorMessageRes The resource ID of the error message.
     * @return An error result with the specified resource ID for the error message.
     */
    public static <T> Result<T> error(@StringRes int errorMessageRes) {
        return new Result<>(null, errorMessageRes);
    }

    /**
     * Factory method to create an error result with an error and a resource ID for the error message.
     *
     * @param <T>              The type of data.
     * @param error            The error that occurred.
     * @param errorMessageRes The resource ID of the error message.
     * @return An error result with the specified error and resource ID for the error message.
     */
    public static <T> Result<T> error(Throwable error, @StringRes int errorMessageRes) {
        return new Result<>(error, errorMessageRes);
    }

    /**
     * Gets the data contained in the result.
     *
     * @return The data if the result is successful, otherwise null.
     */
    public T getData() {
        return data;
    }

    /**
     * Gets the resource ID of the error message.
     *
     * @return The resource ID of the error message.
     */
    @StringRes
    public int getErrorMessageRes() {
        return errorMessageRes;
    }

    /**
     * Gets the error that occurred.
     *
     * @return The error if an error occurred, otherwise null.
     */
    public Throwable getError() {
        return error;
    }

    /**
     * Checks if the result is successful.
     *
     * @return True if the result is successful, otherwise false.
     */
    public boolean isSuccess() {
        return data != null;
    }
}
