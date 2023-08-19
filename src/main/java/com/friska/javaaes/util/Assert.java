package com.friska.javaaes.util;

public class Assert {

    public static void a(boolean bool){
        if(!bool) throw new RuntimeException("Assertion failed");
    }

}
