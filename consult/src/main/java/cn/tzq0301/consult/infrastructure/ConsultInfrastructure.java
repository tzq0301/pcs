package cn.tzq0301.consult.infrastructure;

import cn.tzq0301.consult.entity.Consult;
import cn.tzq0301.consult.repository.ConsultRepository;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author tzq0301
 * @version 1.0
 */
@Repository
@AllArgsConstructor
public class ConsultInfrastructure {
    private final ConsultRepository consultRepository;

    public Mono<Consult> saveConsult(final Consult consult) {
        return consultRepository.save(consult);
    }

    public Flux<Consult> findConsultByStudentId(final String studentId) {
        return consultRepository.findAllByStudentId(studentId);
    }

    public Mono<Consult> findConsultById(final ObjectId id) {
        return consultRepository.findById(id);
    }

    public Mono<Consult> findConsultById(final String id) {
        return consultRepository.findById(new ObjectId(id));
    }

    public Flux<Consult> findAllConsults() {
        return consultRepository.findAll();
    }

    public Flux<Consult> findAllConsultsByConsultorId(final String consultorId) {
        return consultRepository.findAllByConsultorId(consultorId);
    }
}
