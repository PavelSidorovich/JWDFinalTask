package com.sidorovich.pavel.buber.core.util;

import com.sidorovich.pavel.buber.api.util.CoordinateRandomizer;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;
import java.util.stream.DoubleStream;

public class CoordinateRandomizerImpl implements CoordinateRandomizer {

    private static final double LEFT_LONGITUDE_BOUND_OF_MINSK = 27.40676879882813;
    private static final double RIGHT_LONGITUDE_BOUND_OF_MINSK = 27.68692016601563;
    private static final double BOTTOM_LATITUDE_BOUND_OF_MINSK = 53.832675494023555;
    private static final double TOP_LATITUDE_BOUND_OF_MINSK = 53.96739671749269;
    private static final double CENTER_LONGITUDE_OF_MINSK = 27.561435699462894;
    private static final double CENTER_LATITUDE_OF_MINSK = 53.90110181472827;

    private final Random randomizer;

    private CoordinateRandomizerImpl() {
        randomizer = new Random(new Date().getTime());
    }

    private static class Holder {
        private static final CoordinateRandomizerImpl INSTANCE = new CoordinateRandomizerImpl();
    }

    public static CoordinateRandomizerImpl getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public BigDecimal getLongitude() {
        DoubleStream longitude = randomizer.doubles(
                LEFT_LONGITUDE_BOUND_OF_MINSK,
                RIGHT_LONGITUDE_BOUND_OF_MINSK
        );

        return BigDecimal.valueOf(longitude.findFirst().orElse(CENTER_LONGITUDE_OF_MINSK));
    }

    @Override
    public BigDecimal getLatitude() {
        DoubleStream latitude = randomizer.doubles(
                BOTTOM_LATITUDE_BOUND_OF_MINSK,
                TOP_LATITUDE_BOUND_OF_MINSK
        );

        return BigDecimal.valueOf(latitude.findFirst().orElse(CENTER_LATITUDE_OF_MINSK));
    }

}
