package cloudflight.integra.backend.repository;


import cloudflight.integra.backend.entity.Credit;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Simple thread-safe in-memory repository useful for tests / early stages.
 */
@Repository
public class CreditRepositoryInMemoImpl implements CreditRepository {

    private final Map<Long, Credit> store = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(0L);

    /**
     * Saves or updates the given credit.
     *
     * @param credit .
     * @return .
     */
    @Override
    public Credit save(Credit credit) {
        if (credit.getId() == null) {
            long id = idSequence.incrementAndGet();
            credit.setId(id);
        }
        // store a defensive copy? Credit is mutable - for simplicity we store as is.
        store.put(credit.getId(), credit);
        return credit;
    }

    /**
     * Finds a credit by its ID.
     *
     * @param id .
     * @return .
     */
    @Override
    public Optional<Credit> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    /**
     * Finds all credits.
     *
     * @return .
     */
    @Override
    public List<Credit> findAll() {
        return new ArrayList<>(store.values());
    }

    /**
     * Deletes a credit by its ID.
     *
     * @param id .
     */
    @Override
    public void deleteById(Long id) {
        store.remove(id);
    }

    /**
     * Checks if a credit exists by its ID.
     *
     * @param id .
     * @return .
     */
    @Override
    public boolean existsById(Long id) {
        return store.containsKey(id);
    }

    @Override
    public void deleteAll() {
        store.clear();
    }
}
