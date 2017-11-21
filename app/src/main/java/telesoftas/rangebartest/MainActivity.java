package telesoftas.rangebartest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import telesoftas.rangebartest.bar.RangePicker;

public class MainActivity extends AppCompatActivity implements RangePicker.OnRangeChangeListener {
    private TextView start;
    private TextView end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final RangePicker bars = (RangePicker) findViewById(R.id.barsWithThumbs);
        start = (TextView) findViewById(R.id.textStart);
        end = (TextView) findViewById(R.id.textEnd);
        bars.setOnRangeChangeListener(this);
    }

    @Override
    public void onRangeChanged(float startRatio, float endRatio) {
        start.setText(String.valueOf(startRatio));
        end.setText(String.valueOf(endRatio));
    }

    @Override
    public void onFinishedMoving(float startRatio, float endRatio) {
        // Empty
    }
}
