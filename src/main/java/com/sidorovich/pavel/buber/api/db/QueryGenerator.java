package com.sidorovich.pavel.buber.api.db;

import com.sidorovich.pavel.buber.core.db.QueryGeneratorImpl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface QueryGenerator {

    QueryGenerator select(Set<String> columns);

    QueryGenerator insertInto(String table, Set<String> columns);

    QueryGenerator update(String table);

    QueryGenerator delete();

    QueryGenerator values(Collection<Object> values);

    QueryGenerator where(String column, Object value);

    QueryGenerator and(String column, Object value);

    QueryGenerator or(String column, Object value);

    QueryGenerator from(String table);

    QueryGenerator set(Map<String, Object> columnsAndValues);

    Long executeUpdate();

    <T> List<T> fetch(ResultSetExtractor<T> extractor);

    QueryGenerator orderBy(String column);

    QueryGenerator desc();

//    QueryGenerator groupBy(String... columns);

    QueryGenerator innerJoin(String trgTable);

    QueryGenerator on(String srcColumn, String trgColumn);

    QueryGenerator count(String column);

    void reset();

    static QueryGenerator getInstance(ConnectionPool pool){
        return new QueryGeneratorImpl(pool);
    }

}
