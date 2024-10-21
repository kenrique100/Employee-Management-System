package com.Api.EMS.utils;

import java.util.UUID;

public class GUIDGenerator {
    public static String generateGUID(int length) {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, length);
    }
}
