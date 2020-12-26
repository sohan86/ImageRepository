package controller;

import java.util.Date;

public class Util {

    public static void trace(String msg) {
        System.out.println("<T> " + new Date().toLocaleString() + ": " + msg);
    }

    public static void info(String msg) {
        System.out.println("<I> " + new Date().toLocaleString() + ": " + msg);
    }

    public static void warn(String msg) {
        System.out.println("<W> " + new Date().toLocaleString() + ": " + msg);
    }

    public static void error(String msg) {
        System.err.println("<E> " + new Date().toLocaleString() + ": " + msg);
    }

    public static void test(String msg) {
        System.out.println("<X> " + new Date().toLocaleString() + ": " + msg);
    }

}
