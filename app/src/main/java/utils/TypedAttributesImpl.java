package utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.CheckResult;
import android.support.annotation.StyleableRes;
import android.util.AttributeSet;

public class TypedAttributesImpl implements TypedAttributes {
    private final TypedArray attributes;

    public TypedAttributesImpl(
            Context context,
            AttributeSet attributeSet,
            @StyleableRes int[] styleableRes
    ) {
        attributes = context.getTheme()
                .obtainStyledAttributes(attributeSet, styleableRes, 0, 0);
    }

    @CheckResult
    @Override
    public int getLayoutDimension(@StyleableRes int index) {
        return attributes.getLayoutDimension(index, "");
    }

    @Override public void recycle() {
        attributes.recycle();
    }
}
