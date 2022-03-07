package cn.tzq0301.auth.repository;

import cn.tzq0301.user.Role;
import cn.tzq0301.user.Sex;
import cn.tzq0301.auth.user.entity.User;
import cn.tzq0301.auth.user.repository.UserRepository;
import org.bson.types.ObjectId;
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
    void testInsertAssistant() {
        User wx = new User("2019141460400", "WX", "123456", true, Role.ASSISTANT.getRole(), Sex.MALE.getSex(),
                LocalDate.of(2001, 6, 2), "18198646220", "wx@qq.com",
                "111111111111111111", 0);

        userRepository.save(wx)
                .map(User::getName)
                .as(StepVerifier::create)
                .expectNext("WX")
                .verifyComplete();
    }

    @Test
    @Disabled
    void testUpdateWx() {
        User wx = new User("2019141460400", "WX", "123456", true, Role.ASSISTANT.getRole(), Sex.MALE.getSex(),
                LocalDate.of(2001, 6, 2), "18198646220", "wx@gmail.com",
                "111111111111111111", 0);

//        userRepository.save(wx)
//                .map(User::getEmail)
//                .as(StepVerifier::create)
//                .expectNext("wx@gmail.com")
//                .verifyComplete();

        userRepository.save(wx)
                .flatMap(user -> userRepository.findByUserId("2019141460400"))
                .map(User::getEmail)
                .as(StepVerifier::create)
                .expectNext("wx@gmail.com")
                .verifyComplete();
    }

    @Test
    @Disabled
    void testFindByUserId() {
        userRepository.findByUserId("2019141460542")
                .doOnNext(System.out::println)
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

    @Test
    void test() {
        userRepository.findById(new ObjectId("622563b587d5945143d92b13"))
                .doOnNext(System.out::println)
                .map(User::getName)
                .as(StepVerifier::create)
                .expectNext("Student")
                .verifyComplete();
    }
}