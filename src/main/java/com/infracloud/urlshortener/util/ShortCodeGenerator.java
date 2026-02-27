package com.infracloud.urlshortener.util;
import java.security.SecureRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ShortCodeGenerator {

    private static final String CHAR_POOL =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private static final int CODE_LENGTH = 7; 
    private static final SecureRandom RANDOM = new SecureRandom();

    private ShortCodeGenerator() {
       
    }

    public static String generate() {

        return IntStream.range(0, CODE_LENGTH)
                .mapToObj(i -> CHAR_POOL.charAt(RANDOM.nextInt(CHAR_POOL.length())))
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

}