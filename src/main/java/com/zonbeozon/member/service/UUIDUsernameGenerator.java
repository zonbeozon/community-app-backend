package com.zonbeozon.member.service;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
abstract class UUIDUsernameGenerator {
    public static String generate() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
