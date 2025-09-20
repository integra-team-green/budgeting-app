package cloudflight.integra.backend.repository;

import cloudflight.integra.backend.entity.Payment;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Repository
public class DBPaymentRepository implements JpaRepository<Payment, Long> {
    @Override
    public void flush() {

    }

    @Override
    public <S extends Payment> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Payment> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<Payment> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Payment getOne(Long aLong) {
        return null;
    }

    @Override
    public Payment getById(Long aLong) {
        return null;
    }

    @Override
    public Payment getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Payment> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Payment> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends Payment> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends Payment> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Payment> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Payment> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Payment, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends Payment> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Payment> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public Optional<Payment> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public List<Payment> findAll() {
        return List.of();
    }

    @Override
    public List<Payment> findAllById(Iterable<Long> longs) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Payment entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Payment> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Payment> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<Payment> findAll(Pageable pageable) {
        return null;
    }

    public <T> ScopedValue<T> update(Payment payment) {
    }
}
