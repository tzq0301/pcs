package cn.tzq0301.visit.record.repository;

import cn.tzq0301.visit.record.entity.VisitRecord;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tzq0301
 * @version 1.0
 */
@SpringBootTest
class VisitRecordRepositoryTest {
    @Autowired
    VisitRecordRepository visitRecordRepository;

    @Test
    void test() {
        visitRecordRepository.findById(new ObjectId("622365e9767e3c73408923f0"))
                .doOnNext(System.out::println)
                .map(VisitRecord::getDangerLevel)
                .as(StepVerifier::create)
                .expectNext(2)
                .verifyComplete();

        visitRecordRepository.findAll()
                .doOnNext(System.out::println)
                .subscribe();
    }
}