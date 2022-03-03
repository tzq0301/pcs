//package cn.tzq0301.auth.user;
//
//import cn.tzq0301.auth.entity.user.User;
//import cn.tzq0301.user.Role;
//import cn.tzq0301.user.Sex;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import reactor.test.StepVerifier;
//
//import java.time.LocalDate;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * @author tzq0301
// * @version 1.0
// */
//@SpringBootTest
//class UserServiceTest {
//    @Autowired
//    UserService userService;
//
//    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//
//    @Test
//    void findByUserId() {
//    }
//
//    @Test
//    @Disabled
//    void saveUser() {
//        User tzq = new User("2019141460542", "TZQ", passwordEncoder.encode("123456"), true, Role.STUDENT.getRole(), Sex.MALE.getSex(),
//            LocalDate.of(2001, 3, 1), "18968909990", "tzq0301@gmail.com",
//            "330302200103011610", 0);
//
//        userService.saveUser(tzq)
//                .map(User::getName)
//                .as(StepVerifier::create)
//                .expectNext("TZQ")
//                .verifyComplete();
//
//        User dj = new User("2019141040272", "DJ", passwordEncoder.encode("123456"), true, Role.ADMIN.getRole(), Sex.FEMALE.getSex(),
//                LocalDate.of(2001, 3, 1), "18227690705", "1614920881@qq.com",
//                "513823200101225223", 0);
//
//        userService.saveUser(dj)
//                .map(User::getName)
//                .as(StepVerifier::create)
//                .expectNext("DJ")
//                .verifyComplete();
//    }
//}