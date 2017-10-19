package utils;

import android.support.annotation.CheckResult;

public interface TypedAttributes {
    @CheckResult
    int getLayoutDimension(int index);

    void recycle();
}
