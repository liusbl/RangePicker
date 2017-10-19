package telesoftas.rangebartest.bar;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import telesoftas.rangebartest.R;
import utils.TypedAttributes;
import utils.TypedAttributesImpl;

public class RangeBar extends View {
    private Bar bar;

    public RangeBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RangeBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        TypedAttributes attributes = new TypedAttributesImpl(context,
                attributeSet, R.styleable.RangeBar);
        int length = attributes.getLayoutDimension(R.styleable.RangeBar_length);
        int width = attributes.getLayoutDimension(R.styleable.RangeBar_width) * 2;
        attributes.recycle();
        bar = new Bar(length, width, ContextCompat.getColor(getContext(), R.color.colorAccent));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        bar.draw(canvas);
    }
}
