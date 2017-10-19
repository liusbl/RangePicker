package telesoftas.rangebartest.bar;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import telesoftas.rangebartest.R;

public class RangeBarr extends View {
    private Bar bar;

    public RangeBarr(Context context) {
        super(context);
        init();
    }

    public RangeBarr(Context context,
                     @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RangeBarr(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        bar = new Bar(256, 55, ContextCompat.getColor(getContext(), R.color.colorAccent));
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        bar.draw(canvas);
    }
}
