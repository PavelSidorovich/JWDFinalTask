package com.sidorovich.pavel.buber.api.controller;

import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

public interface CommandRequest {

    void addAttributeToJsp(String name, Object attribute);

    String getParameter(String name);

    void createSession();

    boolean sessionExists();

    boolean addToSession(String name, Object value);

    Optional<Object> retrieveFromSession(String name);

    void clearSession();

    Collection<Part> getParts() throws ServletException, IOException;

    Part getPart(String name) throws ServletException, IOException;

}
