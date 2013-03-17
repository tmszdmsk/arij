package com.tadamski.arij.util;

/**
 *
 * @author t.adamski
 */
public class Result<T> {

    private T result;
    private Throwable exception;

    public Result(T result) {
        this.result = result;

    }

    public Result(Throwable exception) {
        this.exception = exception;

    }

    public boolean success() {
        return exception == null;
    }

    public T getResult() {
        return result;
    }

    public Throwable getException() {
        return exception;
    }
}
