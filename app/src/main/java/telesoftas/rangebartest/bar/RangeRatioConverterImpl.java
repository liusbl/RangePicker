package telesoftas.rangebartest.bar;

class RangeRatioConverterImpl implements RangeRatioConverter {
    private Range range;
    private int increment;

    @Override
    public void setRange(Range range, int increment) {
        this.range = range;
        this.increment = increment;
    }

    @Override
    public double convertToValueInRange(float ratio) {
        if (range != null) {
            double difference = range.getEnd() - range.getStart();
            double value = difference * ratio;
            long value2 = Math.round(range.getStart() + value);

            if (increment != 0) {

                long modulo = value2 % increment;
                if (modulo >= increment / 2) {
                    value2 = value2 - modulo + increment;
                } else if (modulo < increment / 2) {
                    value2 = value2 - modulo;
                }
            }

            return value2;
        } else {
            return 0;
        }
    }
}
