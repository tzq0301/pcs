package cn.tzq0301.visit.apply.reposiroty;

import cn.tzq0301.visit.apply.entity.Apply;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * @author tzq0301
 * @version 1.0
 */
public interface ApplyRepository extends ReactiveMongoRepository<Apply, String> {
}
