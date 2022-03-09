package cn.tzq0301.statics.service;

import cn.tzq0301.statics.entity.StaticsInfo;
import cn.tzq0301.statics.manager.StaticsManager;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author tzq0301
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class StaticsService {
    private final StaticsManager staticsManager;

    public Mono<List<StaticsInfo>> listAllStaticsInfos() {
        return staticsManager.listAllStaticsInfos();
    }
}
