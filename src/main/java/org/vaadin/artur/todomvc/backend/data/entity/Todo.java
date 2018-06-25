package org.vaadin.artur.todomvc.backend.data.entity;

import javax.persistence.Entity;
import javax.validation.constraints.Size;

@Entity
public class Todo extends AbstractEntity {

    @Size(min = 1, max = 255)
    private String text;

    private Boolean completed;

    public Todo() {
    }

    public Todo(String text) {
        super();
        this.text = text;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean isCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((getText() == null) ? 0 : getText().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Todo)) {
            return false;
        }
        Todo other = (Todo) obj;
        if (!getId().equals(other.getId())) {
            return false;
        }
        if (!getText().equals(other.getText())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Todo [text=" + getText() + ", completed=" + isCompleted() + ", id=" + getId() + "]";
    }
}
