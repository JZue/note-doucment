package com.jvm.demo.classloader;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class UnsafeDemo {


    private static int ASHIFT;
    private static long ABASE;

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        Unsafe unsafe = (Unsafe) f.get(null);

        String[] array1 = new String[]{"abc", "efg", "hij", "kl", "mn", "xyz"};
        String[] array2 = new String[]{"abc1", "efg1", "hij1", "kl1", "mn1", "xyz1"};
        Class<?> ak = String[].class;
        ABASE = unsafe.arrayBaseOffset(ak);
        int scale = unsafe.arrayIndexScale(ak);
        ASHIFT = 31 - Integer.numberOfLeadingZeros(scale);
        String array11 = (String) unsafe.getObject(array1, ((long) 2 << ASHIFT) + ABASE);
        String array21 = (String) unsafe.getObject(array2, ((long) 2 << ASHIFT) + ABASE);
        System.out.println(ABASE);
        System.out.println(scale);
        System.out.println(ASHIFT);
        System.out.println(array11);
        System.out.println(array21);
    }
}