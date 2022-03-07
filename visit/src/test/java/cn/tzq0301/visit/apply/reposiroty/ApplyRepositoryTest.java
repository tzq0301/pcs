package cn.tzq0301.visit.apply.reposiroty;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tzq0301
 * @version 1.0
 */
@SpringBootTest
class ApplyRepositoryTest {
    @Autowired
    ApplyRepository applyRepository;

    @Test
    void test() {
        applyRepository.findById(new ObjectId("622365e9767e3c73408923f0"))
                .doOnNext(System.out::println)
                .subscribe();
    }

}