package com.sidorovich.pavel.buber.api.service;

public interface ServiceFactory {

    <R> R serviceFor(Class<R> modelClass);

}
