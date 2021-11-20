package com.sidorovich.pavel.buber.api.db;

import com.sidorovich.pavel.buber.exception.EntityExtractionFailedException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@FunctionalInterface
public interface ResultSetExtractor<T> {

    T extract(ResultSet resultSet) throws EntityExtractionFailedException;

    default List<T> extractAll(ResultSet resultSet) throws SQLException, EntityExtractionFailedException {
        List<T> entities = new ArrayList<>();
        while (resultSet.next()) {
            final T entity = this.extract(resultSet);
            entities.add(entity);
        }
        return entities;
    }

}
