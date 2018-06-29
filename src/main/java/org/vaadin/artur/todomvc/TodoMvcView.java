package org.vaadin.artur.todomvc;

import java.util.List;
import java.util.Optional;

import org.vaadin.artur.todomvc.TodoMvcPresenter.Type;
import org.vaadin.artur.todomvc.backend.data.entity.Todo;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UIDetachedException;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.InitialPageSettings.Position;
import com.vaadin.flow.server.InitialPageSettings.WrapMode;
import com.vaadin.flow.server.PageConfigurator;
import com.vaadin.flow.templatemodel.Encode;
import com.vaadin.flow.templatemodel.TemplateModel;

@Tag("todo-mvc")
@HtmlImport("todo-mvc.html")
@Route("")
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
@Push
public class TodoMvcView extends PolymerTemplate<TodoMvcView.TodoMvcModel> implements PageConfigurator {

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        TodoMvcPresenter.get().addListener(e -> {
            try {
                getUI().get().access(() -> {
                    if (e.getType() == Type.ADD) {
                        getModel().getTodolist().add(e.getTodo());
                    } else if (e.getType() == Type.UPDATE) {
                        Optional<Todo> t = findTodo(e.getTodo());
                        if (t.isPresent()) {
                            t.get().setText(e.getTodo().getText());
                            t.get().setCompleted(e.getTodo().isCompleted());
                        }
                    } else if (e.getType() == Type.REMOVE) {
                        Optional<Todo> t = findTodo(e.getTodo());
                        if (t.isPresent()) {
                            getModel().getTodolist().remove(t.get());
                        }

                    }
                });
            } catch (UIDetachedException ee) {
                e.unregisterListener();
            }
        });

        getModel().setTodolist(TodoMvcPresenter.get().getTodolist());
    }

    private Optional<Todo> findTodo(Todo clientTodo) {
        return getModel().getTodolist().stream().filter(t -> t.equals(clientTodo)).findFirst();
    }

    @ClientCallable
    public void addTodo(String text, boolean completed) {
        TodoMvcPresenter.get().addTodo(text, completed);
    }

    @ClientCallable
    public void updateTodoText(Todo clientTodo, String text) {
        TodoMvcPresenter.get().updateTodo(clientTodo, text);
    }

    @ClientCallable
    public void removeTodo(Todo clientTodo) {
        TodoMvcPresenter.get().removeTodo(clientTodo);
    }

    @ClientCallable
    public void markCompleted(Todo clientTodo, boolean completed) {
        TodoMvcPresenter.get().updateTodo(clientTodo, completed);
    }

    @ClientCallable
    public void resync() {
        // Needed because of https://github.com/vaadin/flow/issues/4080
        getModel().setTodolist(getModel().getTodolist());
    }

    @Override
    public void configurePage(InitialPageSettings settings) {
        settings.addLink(Position.PREPEND, "manifest", "todomvc.webmanifest");
        settings.addInlineWithContents("if ('serviceWorker' in navigator) navigator.serviceWorker.register('sw.js');", WrapMode.JAVASCRIPT);

    }

    public interface TodoMvcModel extends TemplateModel {

        List<Todo> getTodolist();

        @Encode(value = LongToStringEncoder.class, path = "id")
        void setTodolist(List<Todo> todolist);
    }

}
