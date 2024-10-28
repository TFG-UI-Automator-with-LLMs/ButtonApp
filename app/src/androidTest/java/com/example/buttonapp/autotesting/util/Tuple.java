package com.example.buttonapp.autotesting.util;

public class Tuple<X, Y> {
    public final X x;
    public final Y y;

    public Tuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    public X getKey(){
        return x;
    }

    public Y getValue(){
        return y;
    }
}

