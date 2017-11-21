package telesoftas.rangebartest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import telesoftas.rangebartest.bar.Range;
import telesoftas.rangebartest.bar.RangePicker;
import telesoftas.rangebartest.bar.RangeRatioConverter;
import telesoftas.rangebartest.bar.RangeRatioConverterImpl;

public class MainActivity extends AppCompatActivity implements RangePicker.OnRangeChangeListener {
    private TextView start;
    private TextView end;
    private RangeRatioConverter converter = new RangeRatioConverterImpl();
    private ArrayAdapter<Integer> valueAdapter;
    private final List<Integer> values = Arrays.asList(1, 5, 10, 20, 100);
    private Spinner valueSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final RangePicker bars = (RangePicker) findViewById(R.id.barsWithThumbs);
        start = (TextView) findViewById(R.id.textStart);
        end = (TextView) findViewById(R.id.textEnd);
        valueSpinner = (Spinner) findViewById(R.id.spinner);
        valueAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item);
        valueAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        valueAdapter.addAll(values);
        valueAdapter.notifyDataSetChanged();
        valueSpinner.setAdapter(valueAdapter);

        bars.setOnRangeChangeListener(this);
        converter.setRange(new Range(100, 1000), 10);
    }

    @Override
    public void onRangeChanged(float startRatio, float endRatio) {
        double startValue = converter.convertToValueInRange(startRatio);
        double endValue = converter.convertToValueInRange(endRatio);
        Integer increment = (Integer) valueSpinner.getSelectedItem();

        double v = startValue % increment;

        //        if (startValue % increment == 0) {
//            start.setText(String.valueOf(startValue));
//        }
//        if (endValue % increment == 0) {
//            end.setText(String.valueOf(endValue));
//        }
    }

    @Override
    public void onFinishedMoving(float startRatio, float endRatio) {
        // Empty
    }
}
