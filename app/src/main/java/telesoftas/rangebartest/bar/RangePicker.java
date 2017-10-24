package telesoftas.rangebartest.bar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import telesoftas.rangebartest.R;

public class RangePicker extends View {
    private static final int BAR_WIDTH_DEFAULT = 4;
    private static final int OUTER_COLOR_DEFAULT = 0;
    private static final int INNER_COLOR_DEFAULT = 0;
    private static final int THUMB_COLOR_DEFAULT = 0;
    private static final int HORIZONTAL_MARGIN = 8;
    private int outerLength;
    private int thumbRadius;
    private int centerY;
    private float startThumbX;
    private float endThumbX;
    private boolean twoAreMoving;
    private Paint outerBarPaint;
    private Paint innerBarPaint;
    private Paint thumbPaint;
    private OnRangeChangeListener listener;

    public RangePicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RangePicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(@NonNull Context context, AttributeSet attributeSet) {
        TypedArray attributes = context.obtainStyledAttributes(attributeSet,
                R.styleable.RangePicker);
        int width = getWidth(attributes);
        int outerColor = getOuterColor(attributes);
        int innerColor = getInnerColor(attributes);
        int thumbColor = getThumbColor(attributes);
        attributes.recycle();
        createPaint(width, outerColor, innerColor, thumbColor);
    }

    private int getWidth(TypedArray attributes) {
        return attributes.getLayoutDimension(R.styleable.RangePicker_barWidth,
                BAR_WIDTH_DEFAULT);
    }

    private int getOuterColor(TypedArray attributes) {
        return attributes.getColor(R.styleable.RangePicker_outerColor, OUTER_COLOR_DEFAULT);
    }

    private int getInnerColor(TypedArray attributes) {
        return attributes.getColor(R.styleable.RangePicker_innerColor, INNER_COLOR_DEFAULT);
    }

    private int getThumbColor(TypedArray attributes) {
        return attributes.getColor(R.styleable.RangePicker_thumbColor, THUMB_COLOR_DEFAULT);
    }

    private void createPaint(int width, int outerColor, int innerColor, int thumbColor) {
        outerBarPaint = createBarPaint(width, outerColor);
        innerBarPaint = createBarPaint(width, innerColor);
        thumbPaint = createThumbPaint(thumbColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(HORIZONTAL_MARGIN, centerY, outerLength, centerY, outerBarPaint);
        canvas.drawLine(startThumbX, centerY, endThumbX, centerY, innerBarPaint);
        canvas.drawCircle(startThumbX, centerY, thumbRadius, thumbPaint);
        canvas.drawCircle(endThumbX, centerY, thumbRadius, thumbPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                move(event);
                storeState(event);
                return true;
            case MotionEvent.ACTION_MOVE:
                move(event);
                return true;
            case MotionEvent.ACTION_UP:
                twoAreMoving = false;
                return false;
            default:
                return false;
        }
    }

    private void storeState(MotionEvent event) {
        float xCoordinate = event.getX();
        float coordinateDifference = endThumbX - startThumbX;
        double offset = coordinateDifference / 6;
        float halfCoordinateDifference = coordinateDifference / 2;
        float thumbCenter = endThumbX - halfCoordinateDifference;
        float newThumbStart = xCoordinate - halfCoordinateDifference;
        float newThumbEnd = xCoordinate + halfCoordinateDifference;
        if (isCoordinateInThumbCenter(xCoordinate, offset, thumbCenter) &&
                coordinatesAreInBounds(newThumbStart, newThumbEnd)) {
            twoAreMoving = true;
        }
    }

    private void move(MotionEvent event) {
        float xCoordinate = event.getX();
        float coordinateDifference = endThumbX - startThumbX;
        double offset = coordinateDifference / 6;
        float halfCoordinateDifference = coordinateDifference / 2;
        float thumbCenter = endThumbX - halfCoordinateDifference;
        float newThumbStart = xCoordinate - halfCoordinateDifference;
        float newThumbEnd = xCoordinate + halfCoordinateDifference;
        move(xCoordinate, offset, thumbCenter, newThumbStart, newThumbEnd);
    }

    private void move(float xCoordinate, double offset, float thumbCenter,
                      float newThumbStart, float newThumbEnd) {
        if (twoAreMoving) {
            moveTwo(xCoordinate, offset, thumbCenter, newThumbStart, newThumbEnd);
        } else {
            moveOne(xCoordinate, offset, thumbCenter, newThumbStart, newThumbEnd);
        }
    }

    private void moveTwo(float xCoordinate, double offset, float thumbCenter, float newThumbStart, float newThumbEnd) {
        if (areTwoMovingAtEdge(newThumbStart, newThumbEnd)) {
            return;
        }
        if (isCoordinateInThumbCenter(xCoordinate, offset, thumbCenter) &&
                coordinatesAreInBounds(newThumbStart, newThumbEnd)) {
            moveBothThumbs(newThumbStart, newThumbEnd);
        }
        invalidate();
    }

    private void moveOne(float xCoordinate, double offset, float thumbCenter, float newThumbStart, float newThumbEnd) {
        if (coordinatesAreInBounds(xCoordinate, xCoordinate)) {
            moveThumb(xCoordinate);
        }
        invalidate();
    }

    private void moveThumb(float xCoordinate) {
        if (isCloserToStartThumb(xCoordinate)) {
            listener.onStartChanged(createRatio(xCoordinate));
            startThumbX = xCoordinate;
        } else {
            listener.onEndChanged(createRatio(xCoordinate));
            endThumbX = xCoordinate;
        }
    }

    private void moveBothThumbs(float newThumbStart, float newThumbEnd) {
        twoAreMoving = true;
        startThumbX = newThumbStart;
        endThumbX = newThumbEnd;
        listener.onStartChanged(createRatio(startThumbX));
        listener.onEndChanged(createRatio(endThumbX));
    }

    private boolean areTwoMovingAtEdge(float newThumbStart, float newThumbEnd) {
        return twoAreMoving && (newThumbStart < 0 || newThumbEnd > outerLength);
    }

    private boolean coordinatesAreInBounds(float newThumbStart, float newThumbEnd) {
        return newThumbStart > 0 && newThumbEnd < outerLength;
    }

    private boolean isCoordinateInThumbCenter(float xCoordinate, double offset, float thumbCenter) {
        return xCoordinate > thumbCenter - offset && xCoordinate < thumbCenter + offset;
    }

    private float createRatio(float xCoordinate) {
        return xCoordinate / outerLength;
    }

    private boolean isCloserToStartThumb(float xCoordinate) {
        return xCoordinate - startThumbX < endThumbX - xCoordinate;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        outerLength = MeasureSpec.getSize(widthMeasureSpec) - HORIZONTAL_MARGIN;
        int verticalMargin = 32;
        centerY = MeasureSpec.getSize(heightMeasureSpec) / 2 + verticalMargin / 2;
        thumbRadius = centerY / 2;
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