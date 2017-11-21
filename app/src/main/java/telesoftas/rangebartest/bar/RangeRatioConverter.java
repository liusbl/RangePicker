package telesoftas.rangebartest.bar;

public interface RangeRatioConverter {
    void setRange(Range range);

    double convertToValueInRange(float ratio);
}
