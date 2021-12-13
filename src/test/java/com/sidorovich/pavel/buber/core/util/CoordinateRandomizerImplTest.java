package com.sidorovich.pavel.buber.core.util;

import com.sidorovich.pavel.buber.api.util.CoordinateRandomizer;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class CoordinateRandomizerImplTest {

    private final CoordinateRandomizer randomizer = CoordinateRandomizerImpl.getInstance();

    @Test
    public void getLongitude_shouldReturnRandomLongitude_always() {
        assertNotNull(randomizer.getLongitude());
    }

    @Test
    public void getLatitude_shouldReturnRandomLatitude_always() {
        assertNotNull(randomizer.getLatitude());
    }

}