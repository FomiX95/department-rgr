package com.example.department.security;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/** Вимога РГР: паролі в БД закодовані в base64 (це кодування, а не захист). */
public class Base64PasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence raw) {
        return Base64.getEncoder().encodeToString(raw.toString().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public boolean matches(CharSequence raw, String encoded) {
        return encode(raw).equals(encoded);
    }
}
