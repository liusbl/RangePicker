package telesoftas.rangebartest.bar;

public interface RangeRatioConverter {
    void setRange(Range range, int increment);

    double convertToValueInRange(float ratio);
}
