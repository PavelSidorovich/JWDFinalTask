package com.sidorovich.pavel.buber.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface StatementPreparator {

    void accept(PreparedStatement t) throws SQLException;

}
