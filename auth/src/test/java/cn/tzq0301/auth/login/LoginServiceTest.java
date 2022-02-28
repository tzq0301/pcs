package cn.tzq0301.auth.login;

import cn.tzq0301.auth.entity.user.Role;
import cn.tzq0301.auth.entity.user.Sex;
import cn.tzq0301.auth.entity.user.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tzq0301
 * @version 1.0
 */
@SpringBootTest
class LoginServiceTest {
    @Autowired
    LoginService loginService;

    @Test
    void login() {
    }

    @Test
    @Disabled
    void saveUserStudent() {
        User tzq = new User("2019141460542", "TZQ", "123456", true, Role.STUDENT, Sex.MALE,
                LocalDate.of(2001, 3, 1), "18968909990", "tzq0301@gmail.com",
                "330302200103011610", 0);

        loginService.saveUser(tzq)
                .map(User::getName)
                .as(StepVerifier::create)
                .expectNext("TZQ")
                .verifyComplete();
    }

    @Test
    @Disabled
    void saveUserAdmin() {
        User dj = new User("2019141040272", "DJ", "123456", true, Role.ADMIN, Sex.FEMALE,
                LocalDate.of(2001, 3, 1), "18227690705", "1614920881@qq.com",
                "513823200101225223", 0);

        loginService.saveUser(dj)
                .map(User::getName)
                .as(StepVerifier::create)
                .expectNext("DJ")
                .verifyComplete();
    }
}