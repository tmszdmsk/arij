package com.tadamski.arij.util;

/**
 *
 * @author t.adamski
 */
public interface Callback<T> {

    public static final Void VOID = new Void();

    public static class Void {}
    
    public void call(T result);
}

