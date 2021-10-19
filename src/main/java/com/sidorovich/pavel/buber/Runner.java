package com.sidorovich.pavel.buber;

import com.sidorovich.pavel.buber.dao.impl.CoordinatesDao;
import com.sidorovich.pavel.buber.db.ConnectionPool;

import java.sql.SQLException;

public class Runner {

    private final static ConnectionPool CONNECTION_POOL = ConnectionPool.locking();

    public static void main(String[] args) throws InterruptedException, SQLException {

        CONNECTION_POOL.init();


        CoordinatesDao coordinatesDao = new CoordinatesDao(CONNECTION_POOL);
        coordinatesDao.delete(5L);
//        coordinatesDao.create(new Coordinates(BigDecimal.valueOf(24), BigDecimal.valueOf(65)));
        coordinatesDao.readAll().forEach(System.out::println);
//        Coordinates coordinates = coordinatesDao.read(5L).orElse(null);
//        coordinatesDao.update(new Coordinates(BigDecimal.valueOf(7), BigDecimal.valueOf(1466)).withId(5L));

//        Account account = new Account("+375", "mypass", Role.CLIENT);
//
//        AccountDao accountDao = new AccountDao(CONNECTION_POOL);
////        Account account = accountDao.read(1L).orElse(null);
////        account = account.withPasswordHash("new2");
//
//        accountDao.create(account);
//        accountDao.readAll().forEach(System.out::println);


//        SQLGenerator sqlGenerator = new SQLGeneratorImpl(CONNECTION_POOL);
//        sqlGenerator.select("id")
//                    .from("buber.user")
//                    .where("id", "5")
//                    .orderBy("id")
//                    .desc();
//
//        sqlGenerator.update("buber.user")
//                    .set("id", "4", "role", "name")
//                    .innerJoin("buber.account")
//                    .on("user.id", "account.id")
//                    .where("id", "1");
//
//        sqlGenerator.delete()
//                    .from("buber.user")
//                    .where("id", "3")
//                    .or("role", "admin")
//                    .and("client_id", "2");
//
//        sqlGenerator.insertInto("buber.user", "id", "role")
//                    .values("5", "admin");

//        DriverDao dao = new DriverDao();
//        BuberUserDao userDao = new BuberUserDao();
//        CoordinatesDao coordinatesDao = new CoordinatesDao();
//
//        UserOrderBuilder orderBuilder = UserOrderBuilder.getInstance();
//        Optional<UserOrder> result = orderBuilder.setPrice(BigDecimal.valueOf(1000))
//                                                 .setStatus(OrderStatus.IN_PROCESS)
//                                                 .setClient(userDao.read(5L).get())
//                                                 .setDriver(dao.read(8L).get())
//                                                 .setInitialCoordinates(coordinatesDao.read(3L).get())
//                                                 .setEndCoordinates(coordinatesDao.read(4L).get())
//                                                 .getResult();
//
//        UserOrderDao userOrderDao = new UserOrderDao();
//        userOrderDao.create(result.get());

//        TaxiDao taxiDao = new TaxiDao();
//
//        CoordinatesDao coordinatesDao = new CoordinatesDao();
////        Coordinates coordinates1 = coordinatesDao.read(3L).get();
//        Coordinates coordinates2 = coordinatesDao.read(4L).get();
////        taxiDao.create(new Taxi("Renault", "Logan", "1414 ME-5", coordinates1));
//        taxiDao.create(new Taxi("Volkswagen", "Golf", "4537 AO-5", coordinates2));
//        Taxi taxi = taxiDao.readAll().get(1);
//
//        Account account = new Account("+375 29 456-25-51", "pass", Role.DRIVER);
//        BuberUser user = BuberUserBuilder.getInstance()
//                                         .setAccount(account)
//                                         .setFirstName("Svetlana")
//                                         .setLastName("Sidorovich")
//                                         .setEmail("lan@mail.ru")
//                                         .setStatus(UserStatus.ACTIVE)
//                                         .setCash(BigDecimal.valueOf(3000))
//                                         .getResult()
//                                         .orElseThrow(EntityBuildingException::new);
//
//
//
//        Driver driver = DriverBuilder.getInstance()
//                                     .setBuberUser(user)
//                                     .setDriverLicense("her license")
//                                     .setTaxi(taxi)
//                                     .setDriverStatus(DriverStatus.FREE)
//                                     .getResult().orElseThrow(EntityBuildingException::new);
//
//        DriverDao dao = new DriverDao();
//        dao.create(driver);


//
//
//        taxiDao.readAll().forEach(System.out::println);
//        taxiDao.read(1L);
//        taxiDao.delete(1L);
//        taxiDao.delete(new Taxi("Volkswagen", "Golf", "4537 AO-5", coordinates2));

//        coordinatesDao.create(new Coordinates(BigDecimal.valueOf(15.0), BigDecimal.valueOf(14.0)));
//        coordinatesDao.update(2L, new Coordinates(BigDecimal.valueOf(1.0), BigDecimal.valueOf(1.0)));
//        coordinatesDao.delete(1L);
//        coordinatesDao.delete(new Coordinates(BigDecimal.valueOf(1.0), BigDecimal.valueOf(1.0)));
//        coordinatesDao.readAll().forEach(System.out::println);
//        System.out.println(coordinatesDao.read(1L));


//        BonusDao bonusDao = new BonusDao();
//        System.out.println(bonusDao.create(new Bonus(3L, 20d, Date.valueOf(LocalDate.now()))));
//        System.out.println(bonusDao.delete(new Bonus(5L, 10d, Date.valueOf(LocalDate.now()))));
//        System.out.println(bonusDao.update(5L, new Bonus(5L, 10d, Date.valueOf(LocalDate.now()))));
//        System.out.println(bonusDao.delete(4L));
//        System.out.println(bonusDao.read(7L));
//        bonusDao.delete(6L);

//        BuberUserDao buberUserDao = new BuberUserDao();
//        System.out.println(buberUserDao.read(2L));
//        buberUserDao.readAll().stream().forEach(System.out::println);
//
//        BuberUser user = BuberUserBuilder.getInstance()
//                                         .setPhone("+375 29 111-11-01")
//                                         .setPasswordHash("hash12")
//                                         .setRole(Role.CLIENT)
//                                         .setFirstName("Stas")
//                                         .setLastName("Sidorovich")
//                                         .setEmail("stas@mail.ru")
//                                         .setStatus(UserStatus.ACTIVE)
//                                         .setCash(BigDecimal.valueOf(2000))
//                                         .getResult()
//                                         .orElseThrow(UnsupportedOperationException::new);
//        System.out.println(buberUserDao.update(2L, user));
//
//        System.out.println();


//        AccountDao accountDao = new AccountDao();
//        accountDao.create(new Account("+375 29 252-14-08", "hello", Role.CLIENT));
//        BonusDao bonusDao = new BonusDao();
//        bonusDao.create(new Bonus(5L, 10.0, Date.valueOf(LocalDate.now())));

//        Optional<Account> account = accountDao.read(1L);
//        System.out.println(account.get().getId().get());
//        System.out.println(account.get().getPasswordHash());
//        System.out.println(account.get().getRole());
//        System.out.println(account.get().getPhone());


//        List<Account> accounts = accountDao.readAll();
//        for (Account account : accounts) {
//            System.out.println(account.getId().get());
//            System.out.println(account.getPasswordHash());
//            System.out.println(account.getRole());
//            System.out.println(account.getPhone());
//        }
    }


}
