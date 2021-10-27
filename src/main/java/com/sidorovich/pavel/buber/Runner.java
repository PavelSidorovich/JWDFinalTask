package com.sidorovich.pavel.buber;

import com.sidorovich.pavel.buber.dao.EntityDao;
import com.sidorovich.pavel.buber.dao.impl.AccountDao;
import com.sidorovich.pavel.buber.db.ConnectionPool;

import java.sql.SQLException;

public class Runner {

    private final static ConnectionPool CONNECTION_POOL = ConnectionPool.locking();

    public static void main(String[] args) throws InterruptedException, SQLException {

        CONNECTION_POOL.init();

        AccountDao dao = new AccountDao(CONNECTION_POOL);
        dao.readAll().forEach(System.out::println);

        CONNECTION_POOL.shutDown();

//        QueryGenerator queryGenerator = new QueryGeneratorImpl(CONNECTION_POOL);
//        queryGenerator.select("id")
//                    .from("buber.user")
//                    .where("id", "5")
//                    .orderBy("id")
//                    .desc();
//
//        queryGenerator.update("buber.user")
//                    .set("id", "4", "role", "name")
//                    .innerJoin("buber.account")
//                    .on("user.id", "account.id")
//                    .where("id", "1");
//
//        queryGenerator.delete()
//                    .from("buber.user")
//                    .where("id", "3")
//                    .or("role", "admin")
//                    .and("client_id", "2");
//
//        queryGenerator.insertInto("buber.user", "id", "role")
//                    .values("5", "admin");
    }
}
