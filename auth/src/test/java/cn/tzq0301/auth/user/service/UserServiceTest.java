package cn.tzq0301.auth.user.service;

import cn.tzq0301.auth.user.entity.User;
import cn.tzq0301.auth.user.entity.Users;
import cn.tzq0301.user.Role;
import cn.tzq0301.user.Sex;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tzq0301
 * @version 1.0
 */
@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    void isPhoneInEnduranceContainer() {
    }

    @Test
    void findByUserId() {
    }

    @Test
    void updateUser() {
    }

    @Test
    @Disabled
    void saveUser() {
        Mono.zip(
                userService.saveUser(Users.newUser("1111111111111", "Student", "123456", Role.STUDENT,
                        Sex.MALE, LocalDate.of(2001, 3, 1), "18968909990",
                        "111@gmail.com", "111111111111111111")),
                userService.saveUser(Users.newUser("2222222222222", "Admin", "123456", Role.ADMIN,
                        Sex.FEMALE, LocalDate.of(2002, 3, 21), "12227690705",
                        "222@gmail.com", "222222222222222222")),
                userService.saveUser(Users.newUser("3333333333333", "Visitor", "123456", Role.VISITOR,
                        Sex.FEMALE, LocalDate.of(1990, 3, 12), "19822901522",
                        "333@gmail.com", "333333333333333333")),
                userService.saveUser(Users.newUser("4444444444444", "Assistant", "123456", Role.ASSISTANT,
                        Sex.FEMALE, LocalDate.of(2002, 5, 2), "18198646220",
                        "444@gmail.com", "444444444444444444")),
                userService.saveUser(Users.newUser("5555555555555", "Consultant", "123456", Role.CONSULTANT,
                        Sex.FEMALE, LocalDate.of(2002, 7, 1), "13583621700",
                        "555@gmail.com", "555555555555555555"))
        ).subscribe();
    }

    @Test
    @Disabled
    void saveUser2() {
        Mono.zip(
                userService.saveUser(Users.newUser("6666666666666", "Student2", "123456", Role.STUDENT,
                        Sex.MALE, LocalDate.of(2001, 3, 1), "66666666666",
                        "666@gmail.com", "666666666666666666")),
                userService.saveUser(Users.newUser("7777777777777", "Student3", "123456", Role.STUDENT,
                        Sex.FEMALE, LocalDate.of(2002, 3, 21), "7777777777",
                        "777@gmail.com", "777777777777777777")),
                userService.saveUser(Users.newUser("8888888888888", "Visitor2", "123456", Role.VISITOR,
                        Sex.FEMALE, LocalDate.of(1990, 3, 12), "88888888888",
                        "888@gmail.com", "888888888888888888")),
                userService.saveUser(Users.newUser("4444444444444", "Consultant2", "123456", Role.CONSULTANT,
                        Sex.FEMALE, LocalDate.of(2002, 5, 2), "99999999999",
                        "999@gmail.com", "999999999999999999"))
        ).subscribe();
    }
}