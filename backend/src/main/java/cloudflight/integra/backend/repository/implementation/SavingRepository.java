package cloudflight.integra.backend.repository.implementation;

import cloudflight.integra.backend.entity.Saving;
import cloudflight.integra.backend.repository.ISavingRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class SavingRepository implements ISavingRepository<Long, Saving> {
    private final Map<Long, Saving> savings;

    public SavingRepository() {
        this.savings = new HashMap<>();
    }


    @Override
    public Saving findOne(Long id) {

        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        if (!savings.containsKey(id)) {
            throw new IllegalArgumentException("entity with id " + id + " does not exist");
        }
        return savings.get(id);
    }


    @Override
    public Iterable<Saving> findAll() {
        return savings.values();
    }


    @Override
    public void save(Saving entity) {
        if (entity == null) {
            throw new IllegalArgumentException("entity must not be null");
        }
        savings.put(entity.getId(), entity);
    }


    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        if (!savings.containsKey(id)) {
            throw new IllegalArgumentException("entity with id " + id + " does not exist");
        }
        savings.remove(id);
    }


    @Override
    public void update(Saving entity) {
        if (entity == null) {
            throw new IllegalArgumentException("entity must not be null");
        }
        if (!savings.containsKey(entity.getId())) {
            throw new IllegalArgumentException("entity with id " + entity.getId() + " does not exist");
        }
        savings.put(entity.getId(), entity);
    }

}
