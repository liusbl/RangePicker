package utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.CheckResult;
import android.support.annotation.ColorInt;
import android.support.annotation.StyleableRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import telesoftas.rangebartest.R;

public class TypedAttributesImpl implements TypedAttributes {
    private final Context context;
    private final TypedArray attributes;

    public TypedAttributesImpl(
            Context context,
            AttributeSet attributeSet,
            @StyleableRes int[] styleableRes
    ) {
        this.context = context;
        attributes = context.getTheme()
                .obtainStyledAttributes(attributeSet, styleableRes, 0, 0);
    }

    @CheckResult
    @Override
    public int getLayoutDimension(@StyleableRes int index) {
        return attributes.getLayoutDimension(index, "");
    }

    @ColorInt
    @Override
    public int getColor(@StyleableRes int index) {
        return attributes.getColor(index, ContextCompat.getColor(context, R.color.colorPrimary));
    }

    @Override
    public void recycle() {
        attributes.recycle();
    }
}
