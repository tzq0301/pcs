package cn.tzq0301.duty.service;

import cn.tzq0301.duty.entity.duty.Duties;
import cn.tzq0301.duty.entity.duty.Patterns;
import cn.tzq0301.duty.entity.duty.SpecialItems;
import cn.tzq0301.duty.entity.work.WorkItems;
import cn.tzq0301.duty.entity.work.Works;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

/**
 * @author tzq0301
 * @version 1.0
 */
@SpringBootTest
class DutyServiceTest {

    @Autowired
    DutyService dutyService;

    @Test
    void getDutyByUserId() {

    }

    @Test
    @Disabled
    void addRegularDuty() {
        dutyService.addRegularDutyByUserId("3333333333333", Patterns.newPattern(1, 9, "白石桥"))
                .subscribe();
    }

    @Test
    @Disabled
    void addRegularDutiesByUserId() {
        dutyService.addRegularDutiesByUserId("3333333333333",
                        Patterns.newPattern(1, 9, "春熙路"),
                        Patterns.newPattern(1, 10, "太古里"),
                        Patterns.newPattern(3, 10, "白石桥"))
                .subscribe();
    }

    @Test
    @Disabled
    void addSpecialDutyByUserId() {
        dutyService.addSpecialDutyByUserId("3333333333333",
                        SpecialItems.newSpecialItem("20220306", 9, "太古里", 0))
                .subscribe();
    }

    @Test
//    @Disabled
    void add() {
//        Mono.zip(
//                dutyService.addRegularDutiesByUserId("555555555555555555",
//                        Patterns.newPattern(1, 9, "太古里"),
//                        Patterns.newPattern(1, 10, "春熙路"),
//                        Patterns.newPattern(2, 10, "白石桥")),
//                dutyService.addSpecialDutiesByUserId("555555555555555555",
//                        SpecialItems.newSpecialItem("20220306", 10, "太古里", 0),
//                        SpecialItems.newSpecialItem("20220307", 9, "", 1))
//        ).subscribe();

        dutyService.addSpecialDutiesByUserId("5555555555555",
                SpecialItems.newSpecialItem("20220306", 10, "太古里", 0),
                SpecialItems.newSpecialItem("20220307", 9, "", 1)).subscribe();
//        dutyService.addRegularDutiesByUserId("5555555555555",
//                        Patterns.newPattern(1, 9, "太古里"),
//                        Patterns.newPattern(1, 10, "春熙路"),
//                        Patterns.newPattern(2, 10, "白石桥")).subscribe();
    }

    @Test
    @Disabled
    void addSpecialDutiesByUserId() {
        dutyService.addSpecialDutiesByUserId("3333333333333",
                        SpecialItems.newSpecialItem("20220306", 9, "太古里", 0),
                        SpecialItems.newSpecialItem("20220307", 9, "", 1))
                .subscribe();
    }

    @Test
    @Disabled
    void saveDuty() {
        dutyService.saveDuty(Duties.newDuty("3333333333333")).subscribe();
    }

    @Test
    void getWorkByUserId() {
    }

    @Test
    @Disabled
    void saveWork() {
        dutyService.saveWork(Works.newWork("3333333333333")).subscribe();
    }

    @Test
    @Disabled
    void addWorkItem() {
        dutyService.addWorkByUserId("3333333333333", WorkItems.newWorkItem("20220101", 14, "白石桥"))
                .subscribe();
    }

    @Test
    @Disabled
    void deleteWorkItem() {
        dutyService.deleteWorkByUserId("3333333333333", WorkItems.newWorkItem("20220101", 14, "白石桥"))
                .subscribe();
    }
}