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
    private int thumbRadius = 24;
    private Paint outerBarPaint;
    private Paint innerBarPaint;
    private Paint thumbPaint;
    private int centerY;
    private float startThumbX;
    private float endThumbX;
    private OnRangeChangeListener listener;
    private int verticalMargin = 32;
    private int horizontalMargin = 8;
    private boolean isActionDown;
    private boolean isActionMove;
    private boolean twoAreMoving;

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
        int width = attributes.getLayoutDimension(R.styleable.BarsWithThumbs_barWidth);
        int outerColor = attributes.getColor(R.styleable.BarsWithThumbs_outerColor);
        int innerColor = attributes.getColor(R.styleable.BarsWithThumbs_innerColor);
        int thumbColor = attributes.getColor(R.styleable.BarsWithThumbs_thumbColor);
        attributes.recycle();
        createPaint(width, outerColor, innerColor, thumbColor);
    }

    private void createPaint(int width, int outerColor, int innerColor, int thumbColor) {
        outerBarPaint = createBarPaint(width, outerColor);
        innerBarPaint = createBarPaint(width, innerColor);
        thumbPaint = createThumbPaint(thumbColor);
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(horizontalMargin, centerY, outerLength, centerY, outerBarPaint);
        canvas.drawLine(startThumbX, centerY, endThumbX, centerY, innerBarPaint);
        canvas.drawCircle(startThumbX, centerY, thumbRadius, thumbPaint);
        canvas.drawCircle(endThumbX, centerY, thumbRadius, thumbPaint);
    }

    @Override public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            isActionDown = true;
            move(event);
            return true;
        } else if (action == MotionEvent.ACTION_MOVE) {
            isActionMove = true;
            move(event);
            return true;
        } else if (action == MotionEvent.ACTION_UP) {
            isActionDown = false;
            isActionMove = false;
            twoAreMoving = false;
            return false;
        }
        return false;
    }

    private void move(MotionEvent event) {
        float xCoordinate = event.getX();
        float coordinateDifference = endThumbX - startThumbX;
        double offset = coordinateDifference / 6;
        float halfCoordinateDifference = coordinateDifference / 2;
        float thumbCenter = endThumbX - halfCoordinateDifference;
        float start = xCoordinate - halfCoordinateDifference;
        float end = xCoordinate + halfCoordinateDifference;

        if (twoAreMoving && isActionMove && (start < 0 || end > outerLength)) {
            return;
        }
        if (isCoordinateInThumbCenter(xCoordinate, offset, thumbCenter)) {
            if (start > 0 && end < outerLength) {
                startThumbX = start;
                endThumbX = end;
                twoAreMoving = true;
            }
        } else if (xCoordinate > 0 && xCoordinate < outerLength) {
            if (isCloserToStartThumb(xCoordinate)) {
                listener.onStartChanged(createRatio(xCoordinate));
                startThumbX = xCoordinate;
            } else {
                listener.onEndChanged(createRatio(xCoordinate));
                endThumbX = xCoordinate;
            }
        }


//        if (xCoordinate > 0 && xCoordinate < outerLength) {
//            if (isInCenter(xCoordinate)) {
//                listener.onStartChanged(createRatio(xCoordinate));
//                listener.onEndChanged(createRatio(xCoordinate));
//                float coordinateDifference = endThumbX - startThumbX;
//                float half = coordinateDifference / 2;
//                startThumbX = xCoordinate - half;
//                endThumbX = xCoordinate + half;
//            } else if (isCloserToStartThumb(xCoordinate)) {
//                listener.onStartChanged(createRatio(xCoordinate));
//                startThumbX = xCoordinate;
//            } else {
//                listener.onEndChanged(createRatio(xCoordinate));
//                endThumbX = xCoordinate;
//            }
//        }
        invalidate();
    }

    private boolean isCoordinateInThumbCenter(float xCoordinate, double offset, float thumbCenter) {
        return xCoordinate > thumbCenter - offset && xCoordinate < thumbCenter + offset;
    }

    private float createRatio(float xCoordinate) {
        return (outerLength - xCoordinate) / xCoordinate;
    }

    private boolean isCloserToStartThumb(float xCoordinate) {
        return xCoordinate - startThumbX < endThumbX - xCoordinate;
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        outerLength = MeasureSpec.getSize(widthMeasureSpec) - horizontalMargin;
        centerY = MeasureSpec.getSize(heightMeasureSpec) / 2 + verticalMargin / 2;
        startThumbX = outerLength / 4;
        endThumbX = outerLength / 4 * 3;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec + verticalMargin);
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

    public void setListener(OnRangeChangeListener listener) {
        this.listener = listener;
    }

    public interface OnRangeChangeListener {
        void onStartChanged(float ratio);

        void onEndChanged(float ratio);
    }
}
