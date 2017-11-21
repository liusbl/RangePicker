package telesoftas.rangebartest.bar;

public class RangeRatioConverterImpl implements RangeRatioConverter {
    private Range range;

    @Override
    public void setRange(Range range) {
        this.range = range;
    }

    @Override
    public double convertToValueInRange(float ratio) {
        if (range != null) {
            double difference = range.getEnd() - range.getStart();
            double value = difference * ratio;
            return Math.round(range.getStart() + value);
        } else {
            return 0;
        }
    }
}
