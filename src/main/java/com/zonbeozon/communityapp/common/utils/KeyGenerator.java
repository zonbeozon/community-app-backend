package com.zonbeozon.communityapp.common.utils;

import java.util.UUID;

public class KeyGenerator {
    public static String generateKey() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}