package telesoftas.rangebartest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import telesoftas.rangebartest.bar.RangePicker;

public class MainActivity extends AppCompatActivity implements RangePicker.OnRangeChangeListener {
    RangePicker bars;
    TextView start;
    TextView end;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bars = (RangePicker) findViewById(R.id.barsWithThumbs);
        start = (TextView)findViewById(R.id.textStart);
        end = (TextView)findViewById(R.id.textEnd);
        bars.setListener(this);
    }

    @Override public void onStartChanged(float ratio) {
        start.setText(String.valueOf(ratio));
    }

    @Override public void onEndChanged(float ratio) {
        end.setText(String.valueOf(ratio));
    }
}
