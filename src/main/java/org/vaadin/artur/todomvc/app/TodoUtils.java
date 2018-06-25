package org.vaadin.artur.todomvc.app;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.vaadin.artur.todomvc.backend.TodoRepository;
import org.vaadin.artur.todomvc.backend.data.entity.Todo;
import org.vaadin.artur.todomvc.backend.service.TodoService;

@Singleton
@Startup
public class TodoUtils implements HasLogger {

    private static TodoService staticTodoService;
    private final TodoRepository todoRepository;
    private final TodoService todoService;

    public TodoUtils() {
        // An empty constructor is required by the EJB spec even though the
        // @Inject constructor is used
        todoRepository = null;
        todoService = null;
        staticTodoService = null;
    }

    @Inject
    public TodoUtils(TodoRepository todoRepository, TodoService todoService) {
        this.todoRepository = todoRepository;
        this.todoService = todoService;
        staticTodoService = todoService;
    }

    public static TodoService getTodoService() {
        return staticTodoService;
    }

    @PostConstruct
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void generate() {
        if (hasData()) {
            getLogger().info("Using existing database");
            return;
        }
        getLogger().info("Generating demo data");
        getLogger().info("... generating Todos");
        createTodos();

        getLogger().info("Generated demo data");
    }

    public boolean hasData() {
        return todoRepository.count() != 0L;
    }

    public void createTodos() {
        for (int i = 0; i < 10; i++) {
            Todo todo = new Todo();
            todo.setText("Todo " + i);
            todoRepository.save(todo);
        }
    }

}
