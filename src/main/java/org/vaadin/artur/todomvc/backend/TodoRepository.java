package org.vaadin.artur.todomvc.backend;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.QueryResult;
import org.apache.deltaspike.data.api.Repository;
import org.vaadin.artur.todomvc.backend.data.entity.Todo;

@Repository
public interface TodoRepository extends EntityRepository<Todo, Long> {

    QueryResult<Todo> findByTextLikeIgnoreCase(String text);

    @Query("select count(e) from Todo e WHERE lower(e.text) like lower(?1)")
    int countByTextLikeIgnoreCase(String text);

    @Query("select e from Todo e")
    QueryResult<Todo> queryAll();

}