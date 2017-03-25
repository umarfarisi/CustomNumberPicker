package mamen.com.circlelistview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.NumberPicker;

/**
 * @author Muhammad Umar Farisi
 * @created 24/03/2017
 */
public class JJBTNumberPicker extends NumberPicker {


    public JJBTNumberPicker(Context context) {
        super(context);
    }

    public JJBTNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JJBTNumberPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public JJBTNumberPicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
