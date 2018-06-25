package org.vaadin.artur.todomvc.backend.service;

import javax.ejb.ApplicationException;

/**
 * A data integraty violation exception containing a message intended to be
 * shown to the end user.
 */
@ApplicationException
public class UserFriendlyDataException extends RuntimeException {

    public UserFriendlyDataException(String message) {
        super(message);
    }

}
