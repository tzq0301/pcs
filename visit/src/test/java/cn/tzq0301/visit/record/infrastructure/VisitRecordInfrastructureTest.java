package cn.tzq0301.visit.record.infrastructure;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author tzq0301
 * @version 1.0
 */
@SpringBootTest
class VisitRecordInfrastructureTest {
    @Autowired
    VisitRecordInfrastructure visitRecordInfrastructure;

    @Test
    void findVisitRecordById() {
        visitRecordInfrastructure.findVisitRecordById(new ObjectId("622365e9767e3c73408923f0"))
                .doOnNext(System.out::println)
                .subscribe();
    }
}