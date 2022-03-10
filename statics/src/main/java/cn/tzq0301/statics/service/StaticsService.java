package cn.tzq0301.statics.service;

import cn.tzq0301.statics.entity.CsvInfo;
import cn.tzq0301.statics.entity.StaticsInfo;
import cn.tzq0301.statics.manager.StaticsManager;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author tzq0301
 * @version 1.0
 */
@Service
@AllArgsConstructor
@Log4j2
public class StaticsService {
    private final StaticsManager staticsManager;

    public Mono<List<StaticsInfo>> listAllStaticsInfos() {
        return staticsManager.listAllStaticsInfos();
    }

    public Mono<String> exportConsultReport(final String globalId) {
        return Mono.just(globalId)
                .flatMap(staticsManager::findPdfInfoByGlobalId)
                .doOnNext(pdfInfo -> log.info("Got information for Export PDF -> {}", pdfInfo))
                .flatMap(staticsManager::exportPdf)
                .doOnNext(url -> log.info("Got URL of PDF -> {}", url));
    }

    public Mono<String> exportConsultorInfo() {
        return Mono.just(staticsManager.listConsultorInfos().flatMap(userInfo ->
                        staticsManager.findConsultorStaticsInfo(userInfo.getId())
                                .map(consultorStaticsInfo -> new CsvInfo(userInfo.getName(),
                                        userInfo.getSex(), userInfo.getPhone(),
                                        userInfo.getEmail(), consultorStaticsInfo.getNumOfPeople(),
                                        consultorStaticsInfo.getNumOfTime()))))
                .flatMap(staticsManager::exportCsv);
    }
}
