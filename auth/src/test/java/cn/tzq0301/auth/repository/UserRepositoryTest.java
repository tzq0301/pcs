package cn.tzq0301.auth.repository;

import cn.tzq0301.user.Role;
import cn.tzq0301.user.Sex;
import cn.tzq0301.auth.entity.user.User;
import cn.tzq0301.auth.user.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.test.StepVerifier;

import java.time.LocalDate;

/**
 * @author tzq0301
 * @version 1.0
 */
@DataMongoTest
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    @Disabled
    void testInsertion() {
        User tzq = new User("2019141460542", "TZQ", "123456", true, Role.STUDENT.getRole(), Sex.MALE.getSex(),
                LocalDate.of(2001, 3, 1), "18968909990", "tzq0301@gmail.com",
                "330302200103011610", 0);

        userRepository.save(tzq)
                .map(User::getName)
                .as(StepVerifier::create)
                .expectNext("TZQ")
                .verifyComplete();
    }

    @Test
    @Disabled
    void testFindByUserId() {
        userRepository.findByUserId("2019141460542")
                .map(User::getName)
                .as(StepVerifier::create)
                .expectNext("TZQ")
                .verifyComplete();
    }

    @Test
    @Disabled
    void testFindNothing() {
        userRepository.findByUserId("2019101460542")
                .as(StepVerifier::create)
                .verifyComplete();
    }
}