package com.friska.javaaes.util;

public class Assert {
    //TODO delete all assertions
    public static void a(boolean bool){
        if(!bool) throw new RuntimeException("Assertion failed");
    }

}
