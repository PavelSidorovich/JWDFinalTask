package com.sidorovich.pavel.buber.db;

import java.util.List;

public interface SQLGenerator {

    SQLGenerator select(String... columns);

    SQLGenerator insertInto(String table, String... columns);

    SQLGenerator update(String table);

    SQLGenerator delete();

    SQLGenerator values(String... values);

    SQLGenerator where(String column, String value);

    SQLGenerator and(String column, String value);

    SQLGenerator or(String column, String value);

    SQLGenerator from(String table);

    SQLGenerator set(String... columns);

    int executeUpdate() throws InterruptedException;

    <T> List<T> fetch(ResultSetExtractor<T> extractor) throws InterruptedException;

    SQLGenerator orderBy(String column);

    SQLGenerator desc();

//    SQLGenerator groupBy(String... columns);

    SQLGenerator innerJoin(String trgTable);

    SQLGenerator on(String srcColumn, String trgColumn);

    void reset();

}
