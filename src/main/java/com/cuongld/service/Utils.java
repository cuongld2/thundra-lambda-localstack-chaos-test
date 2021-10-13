package com.cuongld.service;

import java.util.UUID;


public final class Utils {

    private Utils() {
        throw new UnsupportedOperationException("Not create instance from Utils class");
    }

    public static String generateShortUuid() {
        return UUID.randomUUID().toString().substring(0, 8);
    }



}
