package com.sidorovich.pavel.buber.model.impl;

import com.sidorovich.pavel.buber.model.Builder;
import com.sidorovich.pavel.buber.model.Role;
import com.sidorovich.pavel.buber.model.UserStatus;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Singleton
 */
public class DriverBuilder implements Builder<Driver> {

    private final BuberBuilder userBuilder;
    private String driverLicense;
    private Taxi taxi;

    private DriverBuilder() {
        userBuilder = BuberBuilder.getInstance();
    }

    private static class InstanceCreator {
        static DriverBuilder INSTANCE = new DriverBuilder();
    }

    public static DriverBuilder getInstance() {
        return DriverBuilder.InstanceCreator.INSTANCE;
    }

    @Override
    public void setId(Integer id) {
        userBuilder.setId(id);
    }

    public void setPhone(String phone) {
        userBuilder.setPhone(phone);
    }

    public void setPasswordHash(String passwordHash) {
        userBuilder.setPasswordHash(passwordHash);
    }

    public void setRole(Role role) {
        userBuilder.setRole(role);
    }

    public void setFirstName(String firstName) {
        userBuilder.setFirstName(firstName);
    }

    public void setLastName(String lastName) {
        userBuilder.setLastName(lastName);
    }

    public void setEmail(String email) {
        userBuilder.setEmail(email);
    }

    public void setCash(BigDecimal cash) {
        userBuilder.setCash(cash);
    }

    public void setStatus(UserStatus status) {
        userBuilder.setStatus(status);
    }

    public void setDriverLicense(String driverLicense) {
        this.driverLicense = driverLicense;
    }

    public void setTaxi(Taxi taxi) {
        this.taxi = taxi;
    }

    /**
     * Automatically calls reset() method
     *
     * @return optional value of Driver
     */
    @Override
    public Optional<Driver> getResult() {
        Optional<BuberUser> result = userBuilder.getResult();
        if (taxi == null || driverLicense == null || !result.isPresent()) {
            reset();
            return Optional.empty();
        }
        BuberUser user = result.get();
        Optional<Driver> driver = Optional.of(
                new Driver(
                        user.getId().orElse(null),
                        user.getPhone(),
                        user.getPasswordHash(),
                        user.getRole(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail().orElse(null),
                        user.getCash(),
                        user.getStatus(),
                        this.driverLicense,
                        this.taxi
                )
        );
        reset();
        return driver;
    }

    @Override
    public void reset() {
        userBuilder.reset();
        this.taxi = null;
        this.driverLicense = null;
    }
}
