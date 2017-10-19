package utils;

import android.support.annotation.CheckResult;
import android.support.annotation.ColorInt;
import android.support.annotation.StyleableRes;

public interface TypedAttributes {
    @CheckResult
    int getLayoutDimension(@StyleableRes int index);

    @ColorInt
    @CheckResult
    int getColor(@StyleableRes int index);

    void recycle();
}
