package cn.tzq0301.auth.security;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tzq0301
 * @version 1.0
 */
@SpringBootTest
class PasswordEncoderConfigTest {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @Disabled
    void passwordEncoder() {
        System.out.println(passwordEncoder.encode("123456"));
    }
}