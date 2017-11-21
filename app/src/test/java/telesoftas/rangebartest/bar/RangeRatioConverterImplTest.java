package telesoftas.rangebartest.bar;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RangeRatioConverterImplTest {
    private static final double DELTA = 0.01;
    private Range range = new Range(10, 6000);
    private RangeRatioConverterImpl converter = new RangeRatioConverterImpl();

    @Before
    public void setUp() throws Exception {
        converter.setRange(range, 10);
    }

    @Test
    public void convertToValueInRange_rangeNotGiven_returns0() throws Exception {
        int expected = 0;
        converter = new RangeRatioConverterImpl();

        double actual = converter.convertToValueInRange(0.3f);

        assertEquals("Should return " + expected, expected, actual, DELTA);
    }

    @Test
    public void convertToValueInRange_rangeGiven_ratio0_returns10() throws Exception {
        double expected = 10;

        double actual = converter.convertToValueInRange(0);

        assertRatioConversion(expected, actual);
    }

    @Test
    public void convertToValueInRange_rangeGiven_ratio03_returns1810() throws Exception {
        double expected = 1810;

        double actual = converter.convertToValueInRange(0.3f);

        assertRatioConversion(expected, actual);
    }

    @Test
    public void convertToValueInRange_rangeGiven_ratio1_returns6000() throws Exception {
        double expected = 6000;

        double actual = converter.convertToValueInRange(1);

        assertRatioConversion(expected, actual);
    }

    @Test
    public void convertToValueInRange_rangeGiven_ratio05_returns3010() throws Exception {
        double expected = 3010;

        double actual = converter.convertToValueInRange(0.5f);

        assertRatioConversion(expected, actual);
    }

    @Test
    public void convertToValueInRange_rangeGiven_ratio05_returns2740() throws Exception {
        double expected = 2740;

        double actual = converter.convertToValueInRange(0.456f);

        assertRatioConversion(expected, actual);
    }

    private void assertRatioConversion(double expected, double actual) {
        assertEquals(expected + "(expected) should equal " + actual + "(actual)",
                expected, actual, DELTA);
    }
}