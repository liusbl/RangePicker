package telesoftas.rangebartest.bar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import telesoftas.rangebartest.R;
import utils.TypedAttributes;
import utils.TypedAttributesImpl;

public class Bar extends View {
    private Paint paint;
    private int length;

    public Bar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Bar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(@NonNull Context context, AttributeSet attributeSet) {
        TypedAttributes attributes = new TypedAttributesImpl(context,
                attributeSet, R.styleable.Bar);
        length = attributes.getLayoutDimension(R.styleable.Bar_length);
        int width = attributes.getLayoutDimension(R.styleable.Bar_width) * 2;
        int color = attributes.getColor(R.styleable.Bar_color);
        attributes.recycle();
        paint = createPaint(width, color);
    }

    @NonNull
    private Paint createPaint(float width, @ColorInt int color) {
        Paint paint = new Paint();
        paint.setStrokeWidth(width);
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        return paint;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0, 0, length, 0, paint);
    }
}
