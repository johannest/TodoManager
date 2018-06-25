package org.vaadin.artur.todomvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.vaadin.artur.todomvc.app.TodoUtils;
import org.vaadin.artur.todomvc.backend.data.entity.Todo;
import org.vaadin.artur.todomvc.backend.service.TodoService;
import org.vaadin.artur.todomvc.events.EventBus;
import org.vaadin.artur.todomvc.events.EventBus.Event;
import org.vaadin.artur.todomvc.events.Listener;

import com.vaadin.flow.shared.Registration;

public class TodoMvcPresenter {

    private static TodoMvcPresenter instance = new TodoMvcPresenter();
    private EventBus eventBus = new EventBus();


    private TodoService todoService;

    private TodoMvcPresenter() {
        todoService = null;
    }

    @Inject
    public TodoMvcPresenter(TodoService todoService) {
        this.todoService = todoService;
    }

    public static TodoMvcPresenter get() {
        return instance;
    }

    public List<Todo> getTodolist() {
        if (todoService == null) {
            // TODO: workaround until we have working CDI add-on for Flow
            this.todoService = TodoUtils.getTodoService();
        }
        return Collections.unmodifiableList(todoService.findAll());
    }

    public synchronized Todo addTodo(String text, boolean completed) {
        System.out.println(">> addTodo: " + text);
        Todo todo = new Todo(text);
        todo.setCompleted(completed);
        todo = todoService.save(todo);
        eventBus.fireEvent(new AddUpdateRemoveEvent(Type.ADD, todo));
        return todo;
    }

    public Registration addListener(Listener<AddUpdateRemoveEvent> listener) {
        return eventBus.addListener(AddUpdateRemoveEvent.class, listener);
    }

    public synchronized void updateTodo(Todo clientTodo, String text) {
        Optional<Todo> todo = todoService.load(clientTodo.getId());
        if (!todo.isPresent()) {
            return;
        }

        System.out.println("Todo " + todo.get() + " text updated to " + text);
        todo.get().setText(text);
        Todo savedTodo = todoService.save(todo.get());
        eventBus.fireEvent(new AddUpdateRemoveEvent(Type.UPDATE, savedTodo));
    }

    public synchronized void updateTodo(Todo clientTodo, boolean completed) {
        Optional<Todo> todo = todoService.load(clientTodo.getId());
        if (!todo.isPresent()) {
            return;
        }

        System.out.println("Todo " + todo.get() + " completed updated to " + completed);
        todo.get().setCompleted(completed);
        Todo savedTodo = todoService.save(todo.get());
        eventBus.fireEvent(new AddUpdateRemoveEvent(Type.UPDATE, savedTodo));
    }

    public synchronized void removeTodo(Todo clientTodo) {
        Optional<Todo> todo = todoService.load(clientTodo.getId());
        if (!todo.isPresent()) {
            return;
        }

        System.out.println("Todo " + todo + " removed");
        todoService.delete(todo.get().getId());
        eventBus.fireEvent(new AddUpdateRemoveEvent(Type.REMOVE, todo.get()));
    }

    public enum Type {
        ADD, UPDATE, REMOVE;
    }

    public static class AddUpdateRemoveEvent extends Event {

        private Todo todo;
        private Type type;

        public AddUpdateRemoveEvent(Type type, Todo todo) {
            this.type = type;
            this.todo = todo;
        }

        public Todo getTodo() {
            return todo;
        }

        public Type getType() {
            return type;
        }


    }
}
