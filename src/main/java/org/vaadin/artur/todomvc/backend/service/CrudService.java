package org.vaadin.artur.todomvc.backend.service;

import java.util.List;
import java.util.Optional;

import org.apache.deltaspike.data.api.EntityRepository;
import org.vaadin.artur.todomvc.backend.data.entity.AbstractEntity;

import com.vaadin.flow.data.provider.QuerySortOrder;

public abstract class CrudService<T extends AbstractEntity> {

    protected abstract EntityRepository<T, Long> getRepository();

    public T save(T entity) {
        return getRepository().save(entity);
    }

    public void delete(long id) {
        final Optional<T> entity = load(id);
        if (!entity.isPresent()) {
            throw new UserFriendlyDataException("Entity not found");
        } else {
            getRepository().remove(entity.get());
        }
    }

    public Optional<T> load(long id) {
        final T entity = getRepository().findBy(id);
        if (entity != null) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }

    public abstract long countAnyMatching(Optional<String> filter);

    public abstract List<T> findAnyMatching(Optional<String> filter, int offset, int limit, List<QuerySortOrder> sortOrders);

}
