package telesoftas.rangebartest.bar;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;

public class Bar {
    private final Paint paint;
    private final float length;

    public Bar(int length, float width, @ColorInt int color) {
        this.length = length;
        paint = createPaint(width, color);
    }

    private Paint createPaint(float width, @ColorInt int color) {
        Paint paint = new Paint();
        paint.setStrokeWidth(width);
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        return paint;
    }

    void draw(Canvas canvas) {
        canvas.drawLine(0, 0, length, 0, paint);
    }
}
