package cn.tzq0301.visit;

import cn.tzq0301.visit.apply.infrastructure.ApplyInfrastructure;
import cn.tzq0301.visit.apply.service.ApplyService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

/**
 * @author tzq0301
 * @version 1.0
 */
@SpringBootTest
public class TestApplication {
    @Autowired
    ApplyService applyService;

    @Autowired
    ApplyInfrastructure applyInfrastructure;

    @Test
    @Disabled
    void test() {
        applyInfrastructure.getApplyByApplyId("622365e9767e3c73408923f0")
                .map(apply -> {
                    apply.setApplyPassTime(LocalDate.of(2022, 3, 8));
                    return apply;
                })
                .flatMap(apply -> applyInfrastructure.saveApply(apply))
                .subscribe();
    }
}
