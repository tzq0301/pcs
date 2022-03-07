package cn.tzq0301.visit.record.service;

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
class VisitRecordServiceTest {
    @Autowired
    VisitRecordService visitRecordService;

    @Test
    void findVisitRecordById() {
    }

    @Test
    void testFindVisitRecordById() {
        visitRecordService.findVisitRecordById(new ObjectId("622365e9767e3c73408923f0"))
//        visitRecordService.findVisitRecordById("622365e9767e3c73408923f0")
                .doOnNext(System.out::println)
                .subscribe();
    }

    @Test
    void saveVisitRecord() {
    }

    @Test
    void deleteVisitRecordById() {
    }
}