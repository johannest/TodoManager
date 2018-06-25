package org.vaadin.artur.todomvc.backend.service;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.deltaspike.data.api.QueryResult;
import org.vaadin.artur.todomvc.backend.TodoRepository;
import org.vaadin.artur.todomvc.backend.data.entity.Todo;

import com.vaadin.flow.data.provider.QuerySortOrder;

@Stateless
public class TodoService extends CrudService<Todo> {


    private TodoRepository todoRepository;

    public TodoService() {
        // An empty constructor is required by the EJB spec even though the
        // @Inject constructor is used
        todoRepository = null;
    }

    @Inject
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }


    @Override
    protected TodoRepository getRepository() {
        return todoRepository;
    }

    public List<Todo> findAll() {
        return todoRepository.findAll();
    }

    @Override
    public List<Todo> findAnyMatching(Optional<String> filter, int offset, int limit, List<QuerySortOrder> sortOrders) {
        QueryResult<Todo> result;
        if (filter.isPresent()) {
            String repositoryFilter = "%" + filter.get() + "%";
            result = getRepository().findByTextLikeIgnoreCase(repositoryFilter);
        } else {
            result = getRepository().queryAll();
        }
        result = QueryHelper.applyLimitsAndSortOrder(result, offset, limit, sortOrders);
        return result.getResultList();
    }

    @Override
    public long countAnyMatching(Optional<String> filter) {
        if (filter.isPresent()) {
            String repositoryFilter = "%" + filter.get() + "%";
            return getRepository().countByTextLikeIgnoreCase(repositoryFilter);
        } else {
            return getRepository().count();
        }
    }
}
