package telesoftas.rangebartest.bar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import telesoftas.rangebartest.R;
import utils.TypedAttributes;
import utils.TypedAttributesImpl;

public class BarsWithThumbs extends View {
    private int outerLength;
    private int innerLength;
    private int thumbRadius;
    private Paint outerBarPaint;
    private Paint innerBarPaint;
    private Paint thumbPaint;
    private int height;
    private int centerX;
    private int centerY;
    private float startThumbX;
    private float endThumbX;
    private float innerStartX;
    private float innerEndX;

    public BarsWithThumbs(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BarsWithThumbs(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(@NonNull Context context, AttributeSet attributeSet) {
        TypedAttributes attributes = new TypedAttributesImpl(context,
                attributeSet, R.styleable.BarsWithThumbs);
        int width = attributes.getLayoutDimension(R.styleable.BarsWithThumbs_width);
        outerLength = attributes.getLayoutDimension(R.styleable.BarsWithThumbs_outerLength);
        innerLength = attributes.getLayoutDimension(R.styleable.BarsWithThumbs_innerLength);
        thumbRadius = attributes.getLayoutDimension(R.styleable.BarsWithThumbs_thumbRadius);
        int outerColor = attributes.getColor(R.styleable.BarsWithThumbs_outerColor);
        int innerColor = attributes.getColor(R.styleable.BarsWithThumbs_innerColor);
        int thumbColor = attributes.getColor(R.styleable.BarsWithThumbs_thumbColor);
        attributes.recycle();
        height = thumbRadius * 2;
        centerY = height / 2;
        startThumbX = outerLength / 2 - innerLength / 2;
        endThumbX = outerLength / 2 + innerLength / 2;
        innerStartX = outerLength / 2 - innerLength / 2;
        innerEndX = outerLength / 2 + innerLength / 2;
        createPaint(width, outerColor, innerColor, thumbColor);
    }

    private void createPaint(int width, int outerColor, int innerColor, int thumbColor) {
        outerBarPaint = createBarPaint(width, outerColor);
        innerBarPaint = createBarPaint(width, innerColor);
        thumbPaint = createThumbPaint(thumbColor);
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0, centerY, outerLength, centerY, outerBarPaint);
        canvas.drawLine(innerStartX, thumbRadius, innerEndX, thumbRadius, innerBarPaint);
        canvas.drawCircle(startThumbX, thumbRadius, thumbRadius, thumbPaint);
        canvas.drawCircle(endThumbX, thumbRadius, thumbRadius, thumbPaint);
    }

    @Override public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
            move(event);
            return true;
        } else if (action == MotionEvent.ACTION_CANCEL) {
            return false;
        }
        return false;
    }

    private void move(MotionEvent event) {
        if (isCloserToStartThumb(event)) {
            innerStartX = startThumbX = event.getX();
        } else {
            innerEndX = endThumbX = event.getX();
        }
        invalidate();
    }

    private boolean isCloserToStartThumb(MotionEvent event) {
        return event.getX() - startThumbX < endThumbX - event.getX();
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(outerLength, thumbRadius * 2);
    }

    @NonNull
    private Paint createBarPaint(float width, @ColorInt int color) {
        Paint paint = new Paint();
        paint.setStrokeWidth(width);
        paint.setColor(color);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        return paint;
    }

    @NonNull
    private Paint createThumbPaint(@ColorInt int color) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        return paint;
    }
}
