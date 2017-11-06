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
    private static final int TOUCH_STATE_NONE = -1;
    private static final int TOUCH_STATE_SINGLE = 1;
    private static final int TOUCH_STATE_CENTER = 2;
    private int outerBarStartX;
    private int outerBarEndX;
    private int thumbRadius;
    private int centerY;
    private int touchState = TOUCH_STATE_NONE;
    private float distanceFromStartThumbToTouch;
    private float distanceFromTouchToEndThumb;
    private double thumbCenterSize;
    private boolean isOnMeasureCalled;
    private Paint outerBarPaint;
    private Paint innerBarPaint;
    private Paint thumbPaint;
    private OnRangeChangeListener listener;
    private int startThumbCoordinate;
    private int endThumbCoordinate;

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
            moveStartThumb(outerBarStartX);
            moveEndThumb(outerBarEndX);
        }
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(outerBarStartX, centerY, outerBarEndX, centerY, outerBarPaint);
        canvas.drawLine(startThumbCoordinate, centerY, endThumbCoordinate,
                centerY, innerBarPaint);
        canvas.drawCircle(startThumbCoordinate, centerY, thumbRadius, thumbPaint);
        canvas.drawCircle(endThumbCoordinate, centerY, thumbRadius, thumbPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float currentX = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onActionDown(currentX);
                break;
            case MotionEvent.ACTION_MOVE:
                onActionMove(currentX);
                break;
            case MotionEvent.ACTION_UP:
                onActionUp(currentX);
                break;
            default:
                break;
        }
        return true;
    }

    private void onActionDown(float currentX) {
        touchState = getTouchState(currentX);
        distanceFromStartThumbToTouch = currentX - startThumbCoordinate;
        distanceFromTouchToEndThumb = endThumbCoordinate - currentX;
        updateThumbPositions(currentX);
    }

    private int getTouchState(float currentX) {
        if (isCoordinateInCenterOfThumbs(currentX)) {
            return TOUCH_STATE_CENTER;
        } else {
            return TOUCH_STATE_SINGLE;
        }
    }

    private void onActionMove(float currentX) {
        updateThumbPositions(currentX);
    }

    private void onActionUp(float currentX) {
        updateThumbPositions(currentX);
        touchState = TOUCH_STATE_NONE;
        listener.onFinishedMoving(calculateRatio(startThumbCoordinate),
                calculateRatio(endThumbCoordinate));
    }

    private boolean isCoordinateInCenterOfThumbs(float coordinate) {
        float thumbsCenter = startThumbCoordinate
                + (endThumbCoordinate - startThumbCoordinate) / 2;
        return coordinate > startThumbCoordinate
                && coordinate < endThumbCoordinate
                && coordinate < thumbsCenter + thumbCenterSize
                && coordinate > thumbsCenter - thumbCenterSize;
    }

    private void updateThumbPositions(float currentX) {
        if (currentX <= outerBarStartX) {
            moveStartThumb(outerBarStartX);
        } else if (currentX > outerBarEndX) {
            moveEndThumb(outerBarEndX);
        } else if (touchState == TOUCH_STATE_CENTER) {
            moveBothThumbs(currentX);
        } else if (touchState == TOUCH_STATE_SINGLE) {
            moveClosestThumb(currentX);
        }
        listener.onRangeChanged(calculateRatio(startThumbCoordinate),
                calculateRatio(endThumbCoordinate));
        invalidate();
    }

    private void moveEndThumb(int coordinate) {
        endThumbCoordinate = coordinate;
    }

    private void moveStartThumb(int coordinate) {
        startThumbCoordinate = coordinate;
    }

    private void moveBothThumbs(float coordinate) {
        int newInnerBarStartX = (int) (coordinate - distanceFromStartThumbToTouch);
        int newInnerBarEndX = (int) (coordinate + distanceFromTouchToEndThumb);
        if (newInnerBarStartX > outerBarStartX && newInnerBarEndX < outerBarEndX) {
            moveThumbBar(newInnerBarStartX, newInnerBarEndX);
        } else if (newInnerBarStartX <= outerBarStartX) {
            moveThumbBarToStart();
        } else if (newInnerBarEndX >= outerBarEndX) {
            moveThumbBarToEnd();
        }
    }

    private void moveThumbBar(int startThumbsCoordinate, int endThumbCoordinate) {
        moveStartThumb(startThumbsCoordinate);
        moveEndThumb(endThumbCoordinate);
    }

    private void moveThumbBarToStart() {
        int endThumbPosition = endThumbCoordinate - (startThumbCoordinate - outerBarStartX);
        moveEndThumb(endThumbPosition);
        moveStartThumb(outerBarStartX);
    }

    private void moveThumbBarToEnd() {
        int startThumbPosition = startThumbCoordinate + (outerBarEndX - endThumbCoordinate);
        moveStartThumb(startThumbPosition);
        moveEndThumb(outerBarEndX);
    }

    private void moveClosestThumb(float coordinate) {
        if (coordinate - startThumbCoordinate < endThumbCoordinate - coordinate) {
            moveStartThumb((int) coordinate);
        } else {
            moveEndThumb((int) coordinate);
        }
    }

    private float calculateRatio(float coordinate) {
        return (coordinate - outerBarStartX) / (outerBarEndX - outerBarStartX);
    }

    public void setOnRangeChangeListener(OnRangeChangeListener listener) {
        this.listener = listener;
    }

    public interface OnRangeChangeListener {
        void onRangeChanged(float startRatio, float endRatio);

        void onFinishedMoving(float startRatio, float endRatio);
    }
}