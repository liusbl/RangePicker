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
    private static final int THUMB_RADIUS_DEFAULT = 8;
    private static final int BAR_WIDTH_DEFAULT = 4;
    private static final int OUTER_COLOR_DEFAULT = 0;
    private static final int INNER_COLOR_DEFAULT = 0;
    private static final int THUMB_COLOR_DEFAULT = 0;
    private int outerBarStartX;
    private int outerBarEndX;
    private int thumbRadius;
    private int centerY;
    private float innerBarStartX;
    private float innerBarEndX;
    private float distanceFromStartThumbToTouch;
    private float distanceFromTouchToEndThumb;
    private double thumbCenterSize;
    private boolean twoAreMoving;
    private boolean isMoving;
    private boolean isOnMeasureCalled;
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
        thumbRadius = getThumbRadius(attributes);
        int width = getWidth(attributes);
        int outerColor = getOuterColor(attributes);
        int innerColor = getInnerColor(attributes);
        int thumbColor = getThumbColor(attributes);
        attributes.recycle();
        initializePaint(width, outerColor, innerColor, thumbColor);
    }

    private int getThumbRadius(TypedArray attributes) {
        return attributes.getLayoutDimension(R.styleable.RangePicker_thumbRadius,
                THUMB_RADIUS_DEFAULT);
    }

    private int getWidth(TypedArray attributes) {
        return attributes.getLayoutDimension(R.styleable.RangePicker_barHeight,
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

    private void initializePaint(int width, int outerColor, int innerColor, int thumbColor) {
        outerBarPaint = createBarPaint(width, outerColor);
        innerBarPaint = createBarPaint(width, innerColor);
        thumbPaint = createThumbPaint(thumbColor);
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!isOnMeasureCalled) {
            isOnMeasureCalled = true;
            int fullWidth = MeasureSpec.getSize(widthMeasureSpec);
            int fullHeight = MeasureSpec.getSize(heightMeasureSpec);
            int widthMargin = thumbRadius * 2;
            int outerBarWidth = fullWidth - widthMargin * 2;
            outerBarStartX = widthMargin;
            outerBarEndX = widthMargin + outerBarWidth;
            thumbCenterSize = outerBarWidth * 0.1;
            centerY = fullHeight / 2;
            innerBarStartX = outerBarStartX;
            innerBarEndX = outerBarEndX;
        }
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(outerBarStartX, centerY, outerBarEndX, centerY, outerBarPaint);
        canvas.drawLine(innerBarStartX, centerY, innerBarEndX, centerY, innerBarPaint);
        canvas.drawCircle(innerBarStartX, centerY, thumbRadius, thumbPaint);
        canvas.drawCircle(innerBarEndX, centerY, thumbRadius, thumbPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float currentX = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isMoving = true;
                saveTouchState(currentX);
                move(currentX);
                return true;
            case MotionEvent.ACTION_MOVE:
                isMoving = true;
                move(currentX);
                return true;
            case MotionEvent.ACTION_UP:
                isMoving = false;
                move(currentX);
                twoAreMoving = false;
                return false;
            default:
                return false;
        }
    }

    private void saveTouchState(float currentX) {
        if (shouldMoveBoth(currentX)) {
            twoAreMoving = true;
            distanceFromStartThumbToTouch = currentX - innerBarStartX;
            distanceFromTouchToEndThumb = innerBarEndX - currentX;
        }
    }

    private boolean shouldMoveBoth(float currentX) {
        float innerBarCenter = innerBarStartX + (innerBarEndX - innerBarStartX) / 2;
        return currentX > innerBarStartX &&
                currentX < innerBarEndX &&
                currentX < innerBarCenter + thumbCenterSize &&
                currentX > innerBarCenter - thumbCenterSize;
    }

    private void move(float currentX) {
        if (!areThumbsInBounds(currentX, currentX)) {
            moveToEdge(currentX);
        } else if (twoAreMoving) {
            moveBoth(currentX);
        } else {
            moveOne(currentX);
        }
        listener.onRangeChanged(createRatio(innerBarStartX), createRatio(innerBarEndX));
        invalidate();
    }

    private boolean areThumbsInBounds(float startCoordinate, float endCoordinate) {
        return startCoordinate > outerBarStartX && endCoordinate < outerBarEndX;
    }

    private void moveToEdge(float startCoordinate) {
        if (startCoordinate <= outerBarStartX) {
            innerBarStartX = outerBarStartX;
        } else {
            innerBarEndX = outerBarEndX;
        }
    }

    private void moveBoth(float currentX) {
        float newInnerBarStartX = currentX - distanceFromStartThumbToTouch;
        float newInnerBarEndX = currentX + distanceFromTouchToEndThumb;
        if (areThumbsInBounds(newInnerBarStartX, newInnerBarEndX)) {
            innerBarStartX = newInnerBarStartX;
            innerBarEndX = newInnerBarEndX;
        } else {
            moveToEdge(newInnerBarStartX);
        }
    }

    private void moveOne(float currentX) {
        if (isCloserToStartThumb(currentX)) {
            innerBarStartX = currentX;
        } else {
            innerBarEndX = currentX;
        }
    }

    private boolean isCloserToStartThumb(float currentX) {
        return currentX - innerBarStartX < innerBarEndX - currentX;
    }

    private float createRatio(float coordinate) {
        return (coordinate - outerBarStartX) / (outerBarEndX - outerBarStartX);
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setOnRangeChangeListener(OnRangeChangeListener listener) {
        this.listener = listener;
    }

    public interface OnRangeChangeListener {
        void onRangeChanged(float startRatio, float endRatio);
    }
}